package com.tarena.allrun.view;

import com.tarena.allrun.util.LogUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class MyRelativeLayout extends RelativeLayout{

	public MyRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		LogUtil.i("事件", "MyRelativeLayout dispatchTouchEvent " + ev.getAction());
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		LogUtil.i("事件", "MyRelativeLayout onInterceptTouchEvent " + ev.getAction());

		return super.onInterceptTouchEvent(ev);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		LogUtil.i("事件", "MyRelativeLayout onTouchEvent " + event.getAction());

		return super.onTouchEvent(event);
	}

}
