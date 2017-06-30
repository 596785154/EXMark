package com.tonmx.exmark.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;

public class MyButton extends Button {
	private GestureDetector mGestureDetector;//定义手势对象
    //自定义View的构造器
    public MyButton(Context context) {
        super(context);
    }
    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化GestureDetector对象，传入GestureDetector.SimpleOnGestureListener对象，监听多个手势。
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            //监听双击手势
            @Override
            public boolean onDoubleTap(MotionEvent e) {              
                Log.d("data", "点击了两次按钮！ ");
                return true;
            }
            //监听滑动手势
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(e2.getX() - e1.getX()) > 0) {
                    //设置控件滑动的动画
                    ObjectAnimator.ofFloat(MyButton.this, "translationX", getTranslationX(), getTranslationX() + (e2.getX() - e1.getX())).setDuration(500)
                            .start();
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
            //监听拖动的手势
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                //设置控件跟随手势移动
                setTranslationY(getTranslationY() + e2.getY() - e1.getY());
                setTranslationX(getTranslationX() + e2.getX() - e1.getX());
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //调用此方法，实现手势的监听使用。
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}