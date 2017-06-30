package com.tonmx.exmark.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;

public class ZoomImageView extends ImageView implements OnGlobalLayoutListener,
		OnScaleGestureListener, OnTouchListener {
	private boolean mOnce;

	/**
	 * ��ʼ��ʱ���ŵ�ֵ
	 */
	private float mInitScale;

	/**
	 * ˫���Ŵ�ֵ�����ֵ
	 */
	private float mMidScale;

	/**
	 * �Ŵ�����ֵ
	 */
	private float mMaxScale;

	private Matrix mScaleMatrix;

	/**
	 * �����û���ָ����ʱ���ŵı���
	 */
	private ScaleGestureDetector mScaleGestureDetector;

	// **********�����ƶ��ı���***********
	/**
	 * ��¼��һ�ζ�㴥�ص�����
	 */
	private int mLastPointerCount;

	private float mLastX;
	private float mLastY;
	
	private float mScreenWidth;
	private float mScreenHeight;
	private float myCurrentScale ;

	private int mTouchSlop;
	private boolean isCanDrag;

	private boolean isCheckLeftAndRight;
	private boolean isCheckTopAndBottom;

	// *********˫���Ŵ�����С*********
	private GestureDetector mGestureDetector;

	private boolean isAutoScale;

	public ZoomImageView(Context context) {
		this(context, null);
	}

	public ZoomImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ZoomImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// init
		mScaleMatrix = new Matrix();
		setScaleType(ScaleType.MATRIX);
		setOnTouchListener(this);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mScaleGestureDetector = new ScaleGestureDetector(context, this);
		mGestureDetector = new GestureDetector(context,
				new GestureDetector.SimpleOnGestureListener() {
					@Override
					public boolean onDoubleTap(MotionEvent e) {

						if (isAutoScale) {
							return true;
						}

						float x = e.getX();
						float y = e.getY();

						if (getScale() < mMidScale) {
							postDelayed(new AutoScaleRunnable(mMidScale, x, y),
									16);
							isAutoScale = true;
						} else {
							postDelayed(
									new AutoScaleRunnable(mInitScale, x, y), 16);
							isAutoScale = true;
						}
						return true;
					}
				});
	}

	/**
	 * �Զ��Ŵ�����С
	 * 
	 * 
	 */
	private class AutoScaleRunnable implements Runnable {
		/**
		 * ���ŵ�Ŀ��ֵ
		 */
		private float mTargetScale;
		// ���ŵ����ĵ�
		private float x;
		private float y;

		private final float BIGGER = 1.07f;
		private final float SMALL = 0.93f;

		private float tmpScale;

		/**
		 * @param mTargetScale
		 * @param x
		 * @param y
		 */
		public AutoScaleRunnable(float mTargetScale, float x, float y) {
			this.mTargetScale = mTargetScale;
			this.x = x;
			this.y = y;
			Log.d("Chunna.zheng", "mTargetScale = "+mTargetScale);
			Log.d("Chunna.zheng", "getScale() = "+getScale());
			if (getScale() < mTargetScale) {
				tmpScale = BIGGER;
			}
			if (getScale() > mTargetScale) {
				tmpScale = SMALL;
			}
			
			Log.d("Chunna.zheng", "tmpScale = "+tmpScale);
		}

		@Override
		public void run() {
			// ��������
			mScaleMatrix.postScale(tmpScale, tmpScale, x, y);
			checkBorderAndCenterWhenScale();
			setImageMatrix(mScaleMatrix);

			float currentScale = getScale();
			Log.d("Chunna.zheng", "************currentScale="+currentScale);
			if ((tmpScale > 1.0f && currentScale < mTargetScale)
					|| (tmpScale < 1.0f && currentScale > mTargetScale)) {
				Log.d("Chunna.zheng", "=============");
				// ������������µ���run()����
				postDelayed(this, 16);
			} else {
				// ����Ϊ���ǵ�Ŀ��ֵ
				float scale = mTargetScale / currentScale;
				mScaleMatrix.postScale(scale, scale, x, y);
				Log.d("Chunna.zheng", "scale = mTargetScale / currentScale ="+scale);
				checkBorderAndCenterWhenScale();
				setImageMatrix(mScaleMatrix);

				isAutoScale = false;
			}
		}
	}

	/**
	 * ��ȡImageView������ɵ�ͼƬ
	 */
	@Override
	public void onGlobalLayout() {
		if (!mOnce) {
			// �õ��ؼ��Ŀ�͸�
			int width = getWidth();
			int height = getHeight();

			// �õ����ǵ�ͼƬ���Լ���͸�
			Drawable drawable = getDrawable();
			if (drawable == null) {
				return;
			}
			int dh = drawable.getIntrinsicHeight();
			int dw = drawable.getIntrinsicWidth();

			float scale = 1.0f;

			// ͼƬ�Ŀ�ȴ��ڿؼ��Ŀ�ȣ�ͼƬ�ĸ߶�С�ڿռ�ĸ߶ȣ����ǽ�����С
			if (dw > width && dh < height) {
				scale = width * 1.0f / dw;
			}

			// ͼƬ�Ŀ��С�ڿؼ��Ŀ�ȣ�ͼƬ�ĸ߶ȴ��ڿռ�ĸ߶ȣ����ǽ�����С
			if (dh > height && dw < width) {
				scale = height * 1.0f / dh;
			}

			// ��Сֵ
			if (dw > width && dh > height) {
				scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
			}

			// �Ŵ�ֵ
			if (dw < width && dh < height) {
				scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
			}

			/**
			 * �õ��˳�ʼ��ʱ���ŵı���
			 */
			mInitScale = scale;
			mMaxScale = mInitScale * 4;
			mMidScale = mInitScale * 2;

			// ��ͼƬ�ƶ����ؼ����м�
			int dx = getWidth() / 2 - dw / 2;
			int dy = getHeight() / 2 - dh / 2;

			mScaleMatrix.postTranslate(dx, dy);
			mScaleMatrix.postScale(mInitScale, mInitScale, width / 2,
					height / 2);
			setImageMatrix(mScaleMatrix);

			zoomIn();

			mOnce = true;
		}
	}

	/**
	 * ע��OnGlobalLayoutListener����ӿ�
	 */
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		getViewTreeObserver().addOnGlobalLayoutListener(this);
	}

	/**
	 * ȡ��OnGlobalLayoutListener����ӿ�
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		getViewTreeObserver().removeGlobalOnLayoutListener(this);
	}

	/**
	 * ��ȡ��ǰͼƬ������ֵ
	 * 
	 * @return
	 */
	public float getScale() {
		float[] values = new float[9];
		mScaleMatrix.getValues(values);
		return values[Matrix.MSCALE_X];
	}

	// ��������ʱinitScale maxScale
	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		float scale = getScale();
		float scaleFactor = detector.getScaleFactor();

		if (getDrawable() == null) {
			return true;
		}

		// ���ŷ�Χ�Ŀ���
		if ((scale < mMaxScale && scaleFactor > 1.0f)
				|| (scale > mInitScale && scaleFactor < 1.0f)) {
			if (scale * scaleFactor < mInitScale) {
				scaleFactor = mInitScale / scale;
			}

			if (scale * scaleFactor > mMaxScale) {
				scale = mMaxScale / scale;
			}

			// ����
			mScaleMatrix.postScale(scaleFactor, scaleFactor,
					detector.getFocusX(), detector.getFocusY());

			checkBorderAndCenterWhenScale();

			setImageMatrix(mScaleMatrix);
		}

		return true;
	}

	/**
	 * ���ͼƬ�Ŵ���С�Ժ�Ŀ�͸ߣ��Լ�left��right��top��bottom
	 * 
	 * @return
	 */
	private RectF getMatrixRectF() {
		Matrix matrix = mScaleMatrix;
		RectF rectF = new RectF();
		Drawable d = getDrawable();
		if (d != null) {
			rectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
			matrix.mapRect(rectF);
		}
		return rectF;
	}

	/**
	 * �����ŵ�ʱ����б߽��Լ����ǵ�λ�õĿ���
	 */
	private void checkBorderAndCenterWhenScale() {
		RectF rectF = getMatrixRectF();
		float deltaX = 0;
		float deltaY = 0;

		int width = getWidth();
		int height = getHeight();

		// ����ʱ���б߽��⣬��ֹ���ְױ�
		if (rectF.width() >= width) {
			if (rectF.left > 0) {
				deltaX = -rectF.left;
			}
			if (rectF.right < width) {
				deltaX = width - rectF.right;
			}
		}

		if (rectF.height() >= height) {
			if (rectF.top > 0) {
				deltaY = -rectF.top;
			}
			if (rectF.bottom < height) {
				deltaY = height - rectF.bottom;
			}
		}

		/**
		 * �����Ȼ�߶�С�ڿռ�Ŀ���߸ߣ����������
		 */
		if (rectF.width() < width) {
			deltaX = width / 2f - rectF.right + rectF.width() / 2f;
		}

		if (rectF.height() < height) {
			deltaY = height / 2f - rectF.bottom + rectF.height() / 2f;
		}
		Log.d("Chunna.zheng", "Translate deltaX= "+deltaX);
		Log.d("Chunna.zheng", "Translate deltaY= "+deltaY);
		mScaleMatrix.postTranslate(deltaX, deltaY);
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {

		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (mGestureDetector.onTouchEvent(event)) {
			return true;
		}

		mScaleGestureDetector.onTouchEvent(event);

		float x = 0;
		float y = 0;
		// �õ���㴥�ص�����
		int pointerCount = event.getPointerCount();
		for (int i = 0; i < pointerCount; i++) {
			x += event.getX(i);
			y += event.getY(i);
		}

		x /= pointerCount;
		y /= pointerCount;

		if (mLastPointerCount != pointerCount) {
			isCanDrag = false;
			mLastX = x;
			mLastY = y;
		}
		mLastPointerCount = pointerCount;
		RectF rectF = getMatrixRectF();
		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:
			if (rectF.width() > getWidth() + 0.01
					|| rectF.height() > getHeight() + 0.01) {
				if (getParent() instanceof ViewPager)
					getParent().requestDisallowInterceptTouchEvent(true);
			}
			break;

		case MotionEvent.ACTION_MOVE:
			if (rectF.width() > getWidth() + 0.01
					|| rectF.height() > getHeight() + 0.01) {
				if (getParent() instanceof ViewPager)
					getParent().requestDisallowInterceptTouchEvent(true);
			}
			float dx = x - mLastX;
			float dy = y - mLastY;

			if (!isCanDrag) {
				isCanDrag = isMoveAction(dx, dy);
			}

			if (isCanDrag) {
				if (getDrawable() != null) {
					isCheckLeftAndRight = isCheckTopAndBottom = true;
					// ������С�ڿؼ���ȣ�����������ƶ�
					if (rectF.width() < getWidth()) {
						isCheckLeftAndRight = false;
						dx = 0;
					}
					// ����߶�С�ڿؼ��߶ȣ������������ƶ�
					if (rectF.height() < getHeight()) {
						isCheckTopAndBottom = false;
						dy = 0;
					}
					mScaleMatrix.postTranslate(dx, dy);

					checkBorderWhenTranslate();

					setImageMatrix(mScaleMatrix);
				}
			}
			mLastX = x;
			mLastY = y;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mLastPointerCount = 0;
			break;

		default:
			break;
		}

		return true;
	}

	/**
	 * ���ƶ�ʱ���б߽���
	 */
	private void checkBorderWhenTranslate() {
		RectF rectF = getMatrixRectF();
		float deltaX = 0;
		float deltaY = 0;

		int width = getWidth();
		int heigth = getHeight();

		if (rectF.top > 0 && isCheckTopAndBottom) {
			deltaY = -rectF.top;
		}
		if (rectF.bottom < heigth && isCheckTopAndBottom) {
			deltaY = heigth - rectF.bottom;
		}
		if (rectF.left > 0 && isCheckLeftAndRight) {
			deltaX = -rectF.left;
		}
		if (rectF.right < width && isCheckLeftAndRight) {
			deltaX = width - rectF.right;
		}
		mScaleMatrix.postTranslate(deltaX, deltaY);

	}

	/**
	 * �ж��Ƿ���move
	 * 
	 * @param dx
	 * @param dy
	 * @return
	 */
	private boolean isMoveAction(float dx, float dy) {
		return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
	}
	
	//�Ŵ�
	public void zoomIn(){
		if (isAutoScale) {
			return ;
		}
		float centerX = mScreenWidth/2;
		float centerY = 0;
		Log.d("Chunna.zheng", "in---getScale() = "+getScale());
		Log.d("Chunna.zheng", "in---mInitScale = "+mInitScale);
		Log.d("Chunna.zheng", "in---mMidScale = "+mMidScale);
		Log.d("Chunna.zheng", "in---mMaxScale = "+mMaxScale);
		if (getScale() < mMidScale) {
			postDelayed(new AutoScaleRunnable(mMidScale, centerX, centerY),
					16);
			isAutoScale = true;
		} else if (getScale() < mMaxScale){
			postDelayed(
					new AutoScaleRunnable(mMaxScale, centerX, centerY), 16);
			isAutoScale = true;
		}else if(getScale() >= mMaxScale){
			//ʲô������
		}
	}
	
	//��С
	public void zoomOut(){
		if (isAutoScale) {
			return ;
		}
		float centerX = mScreenWidth/2;
		float centerY = 0;
		Log.d("Chunna.zheng", "out---getScale() = "+getScale());
		Log.d("Chunna.zheng", "out---mInitScale = "+mInitScale);
		Log.d("Chunna.zheng", "out---mMidScale = "+mMidScale);
		Log.d("Chunna.zheng", "out---mMaxScale = "+mMaxScale);
		if(getScale() <= mInitScale){
			//ʲô������
		}else if (getScale() <= mMidScale) {
			postDelayed(new AutoScaleRunnable(mInitScale, centerX, centerY),
					16);
			isAutoScale = true;
		} else if (getScale() <= mMaxScale){
			postDelayed(
					new AutoScaleRunnable(mMidScale, centerX, centerY), 16);
			isAutoScale = true;
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		mScreenWidth = right -left;
		mScreenHeight = bottom - top;
		super.onLayout(changed, left, top, right, bottom);
	}

}