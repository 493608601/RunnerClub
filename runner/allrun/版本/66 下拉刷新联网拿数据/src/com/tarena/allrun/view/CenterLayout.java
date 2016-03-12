package com.tarena.allrun.view;

import com.tarena.allrun.util.LogUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class CenterLayout extends ViewGroup {
	int viewWidth, viewHeight;

	public CenterLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int childViewCount = this.getChildCount();
		LogUtil.i("centerLayout测量", "onMeasure");
		for (int i = 0; i < childViewCount; i++) {
			View view = this.getChildAt(i);
			int size = MeasureSpec.getSize(widthMeasureSpec);
			widthMeasureSpec = MeasureSpec.makeMeasureSpec(size,
					MeasureSpec.AT_MOST);

			size = MeasureSpec.getSize(heightMeasureSpec);
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(size,
					MeasureSpec.AT_MOST);
			view.measure(widthMeasureSpec, heightMeasureSpec);

		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		LogUtil.i("centerLayout测量", "onLayout");

		int childViewCount = this.getChildCount();
		// 不设置测量模式，高度不对
		int sum = 0;
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View view = this.getChildAt(i);
			int height = view.getMeasuredHeight();
			sum = sum + height;
		}

		int y = (viewHeight - sum) / 2;
		for (int i = 0; i < childCount; i++) {
			View view = this.getChildAt(i);
			int height = view.getMeasuredHeight();
			int width = view.getMeasuredWidth();
			int left = (viewWidth - width) / 2;
			view.layout(left, y, left + width, y + height);
			y = y + height;
		}

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		viewWidth = w;
		viewHeight = h;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		LogUtil.i("事件", "centerLayout dispatchTouchEvent " + ev.getAction());
		return super.dispatchTouchEvent(ev);
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		LogUtil.i("事件", "centerLayout onInterceptTouchEvent " + ev.getAction());
		return super.onInterceptTouchEvent(ev);
		//return true;
	}	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		LogUtil.i("事件", "centerLayout onTouchEvent " + event.getAction());
		return super.onTouchEvent(event);
	}

}
