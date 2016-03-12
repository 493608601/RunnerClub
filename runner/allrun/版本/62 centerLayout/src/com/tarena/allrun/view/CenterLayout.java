package com.tarena.allrun.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class CenterLayout extends ViewGroup{
int viewWidth,viewHeight;
	public CenterLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int childCount=getChildCount();
		for (int i=0;i<childCount;i++)
		{
			View view=this.getChildAt(i);
			int width=MeasureSpec.getSize(widthMeasureSpec);
			widthMeasureSpec=MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST);
			
			int height=MeasureSpec.getSize(heightMeasureSpec);
			heightMeasureSpec=MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
			view.measure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int sum=0;
		int childCount=getChildCount();
		for (int i=0;i<childCount;i++)
		{
			View view=this.getChildAt(i);
			int height=view.getMeasuredHeight();
			
			sum=sum+height;
		}
		
		int y=(viewHeight-sum)/2;
		int top=0;
		for (int i=0;i<childCount;i++)
		{
			View view=this.getChildAt(i);
			int width=view.getMeasuredWidth();
			int height=view.getMeasuredHeight();
			int x=(viewWidth-width)/2;
			view.layout(x, y+top, x+width, y+top+height);
			top=top+height;
		}
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		viewWidth=w;
		viewHeight=h;
	}

}
