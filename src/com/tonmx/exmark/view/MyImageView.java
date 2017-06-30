package com.tonmx.exmark.view;

import com.tonmx.exmark.activity.R;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
/** 
 *  ��View������������������Ҫע�⣺
 *  1��View��������longClickableΪtrue����������ʶ���޷���ȷ������ֻ�᷵��Down, Show, Long�������ơ�
 *  2��������View��onTouchListener�е�������ʶ�𣬶�������Activityһ������onTouchEvent������ͬ������ʶ���޷���ȷ������
 * */
public class MyImageView extends ImageView {
	private GestureDetector gesture;
	private ScaleGestureDetector scaleGesture;
	private Matrix matrix;
	private Bitmap bitmap;
	
	private float initScale = 1f;
	private float maxScale = 2f;
	private boolean isInitScale = true;

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.d("Chunna.zheng", "=====ע��gesture=====");
		
		matrix = new Matrix();
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.camera);
		
		gesture = new GestureDetector(context, new MyGestureListener());
		scaleGesture = new ScaleGestureDetector(context, new MyScaleGestureListener());
		setLongClickable(true);
		this.setOnTouchListener(new OnTouchListener() {  
            
            @Override  
            public boolean onTouch(View v, MotionEvent event) {  
                 if(gesture.onTouchEvent(event)){
                	 return true;
                 }  
                 scaleGesture.onTouchEvent(event);
                 return true;
            }  
        }); 
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if (null != bitmap) {
			canvas.save();
			canvas.drawBitmap(bitmap, matrix, null);
			canvas.restore();
		}
		super.onDraw(canvas); 
	}
	
	class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
		
		//����˫������
        @Override
        public boolean onDoubleTap(MotionEvent e) {
        	Log.d("Chunna.zheng", "=====onDoubleTap=====");
        	//matrix.postScale(2f, 2f);
        	if(isInitScale){
        		matrix.setScale(maxScale, maxScale);
        		isInitScale = false;
        	}else{
        		matrix.setScale(initScale, initScale);
        		isInitScale = true;
        	}
        	setImageMatrix(matrix);
        	invalidate();
            return true;
        }
        //������������
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        	Log.d("Chunna.zheng", "=====onFling=====");
            if (Math.abs(e2.getX() - e1.getX()) > 0) {
                //���ÿؼ������Ķ���
                ObjectAnimator.ofFloat(MyImageView.this, "translationX", getTranslationX(), getTranslationX() + (e2.getX() - e1.getX())).setDuration(500)
                        .start();
                return true;
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
        //�����϶�������
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        	Log.d("Chunna.zheng", "=====onScroll=====");
        	int pointCount = e2.getPointerCount();
        	if(1 == pointCount){
        		 //���ÿؼ����������ƶ�
                setTranslationY(getTranslationY() + e2.getY() - e1.getY());
                setTranslationX(getTranslationX() + e2.getX() - e1.getX());
                return true;
        	}
        	return false;
        }
	}
	
	class MyScaleGestureListener implements OnScaleGestureListener{

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			// TODO Auto-generated method stub
			Log.d("Chunna.zheng","onScroll:"+detector.getScaleFactor());  
	        /*if(detector.getScaleFactor()< 1){  
	            return false;  
	        }*/  
	        matrix.preScale(detector.getScaleFactor(), detector.getScaleFactor());  
	        setImageMatrix(matrix);  
	        return true; 
		}

		/**
		 * ���ſ�ʼ����detector�Ƿ����̵������¼�������falseʱ������ִ��onScale()��
		 */
		@Override
		public boolean onScaleBegin(ScaleGestureDetector arg0) {
			// TODO Auto-generated method stub
			Log.d("Chunna.zheng", "=====onScaleBegin=====");
			return true;
		}

		/**
		 * ���Ž���ʱ��
		 */
		@Override
		public void onScaleEnd(ScaleGestureDetector arg0) {
			// TODO Auto-generated method stub
			Log.d("Chunna.zheng", "=====onScaleEnd=====");
		}
		
	}
}