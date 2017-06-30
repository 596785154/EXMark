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
 *  在View中设置手势有两点需要注意：
 *  1：View必须设置longClickable为true，否则手势识别无法正确工作，只会返回Down, Show, Long三种手势。
 *  2：必须在View的onTouchListener中调用手势识别，而不能像Activity一样重载onTouchEvent，否则同样手势识别无法正确工作。
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
		Log.d("Chunna.zheng", "=====注册gesture=====");
		
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
		
		//监听双击手势
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
        //监听滑动手势
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        	Log.d("Chunna.zheng", "=====onFling=====");
            if (Math.abs(e2.getX() - e1.getX()) > 0) {
                //设置控件滑动的动画
                ObjectAnimator.ofFloat(MyImageView.this, "translationX", getTranslationX(), getTranslationX() + (e2.getX() - e1.getX())).setDuration(500)
                        .start();
                return true;
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
        //监听拖动的手势
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        	Log.d("Chunna.zheng", "=====onScroll=====");
        	int pointCount = e2.getPointerCount();
        	if(1 == pointCount){
        		 //设置控件跟随手势移动
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
		 * 缩放开始。该detector是否处理后继的缩放事件。返回false时，不会执行onScale()。
		 */
		@Override
		public boolean onScaleBegin(ScaleGestureDetector arg0) {
			// TODO Auto-generated method stub
			Log.d("Chunna.zheng", "=====onScaleBegin=====");
			return true;
		}

		/**
		 * 缩放结束时。
		 */
		@Override
		public void onScaleEnd(ScaleGestureDetector arg0) {
			// TODO Auto-generated method stub
			Log.d("Chunna.zheng", "=====onScaleEnd=====");
		}
		
	}
}