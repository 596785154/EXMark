package com.tonmx.exmark.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;

public class MyButton extends Button {
	private GestureDetector mGestureDetector;//�������ƶ���
    //�Զ���View�Ĺ�����
    public MyButton(Context context) {
        super(context);
    }
    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        //��ʼ��GestureDetector���󣬴���GestureDetector.SimpleOnGestureListener���󣬼���������ơ�
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            //����˫������
            @Override
            public boolean onDoubleTap(MotionEvent e) {              
                Log.d("data", "��������ΰ�ť�� ");
                return true;
            }
            //������������
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(e2.getX() - e1.getX()) > 0) {
                    //���ÿؼ������Ķ���
                    ObjectAnimator.ofFloat(MyButton.this, "translationX", getTranslationX(), getTranslationX() + (e2.getX() - e1.getX())).setDuration(500)
                            .start();
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
            //�����϶�������
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                //���ÿؼ����������ƶ�
                setTranslationY(getTranslationY() + e2.getY() - e1.getY());
                setTranslationX(getTranslationX() + e2.getX() - e1.getX());
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //���ô˷�����ʵ�����Ƶļ���ʹ�á�
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}