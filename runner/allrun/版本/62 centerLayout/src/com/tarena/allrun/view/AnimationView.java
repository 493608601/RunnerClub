package com.tarena.allrun.view;

import com.baidu.android.bbalbs.common.a.c;
import com.tarena.allrun.R;
import com.tarena.allrun.util.LogUtil;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class AnimationView extends View {
	Bitmap[] bitmaps = null;
	int index = 0;
	Thread thread = null;
	boolean isRunning = true;
	int viewWidth, viewHeight;
	int sleepTime = 100;

	public AnimationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		try {
			TypedArray ta = context.getResources().obtainTypedArray(
					R.array.animationImages);
			int length = ta.length();
			bitmaps = new Bitmap[length];
			for (int i = 0; i < length; i++) {
				int resId = ta.getResourceId(i, -1);
				bitmaps[i] = BitmapFactory
						.decodeResource(getResources(), resId);
			}
			LogUtil.i("AnimationView", bitmaps.length);
			
			TypedArray ta2=context.obtainStyledAttributes(attrs, R.styleable.animation);
			sleepTime=(int) ta2.getFloat(R.styleable.animation_sleepTime, 100);
			
			MyRunnable myRunnable = new MyRunnable();
			thread = new Thread(myRunnable);
			thread.start();
			LogUtil.i("AnimationView", bitmaps.length+",sleepTime="+sleepTime);

			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int mode=MeasureSpec.getMode(widthMeasureSpec);
		if (mode==MeasureSpec.AT_MOST)
		{
			int height=bitmaps[0].getHeight();
			int width=bitmaps[0].getWidth();
			setMeasuredDimension(width, height);
		}
		if (mode==MeasureSpec.EXACTLY)
		{
			LogUtil.i("AnimationView", "EXACTLY");

		}
		LogUtil.i("AnimationView", "heightMeasureSpec=" + heightMeasureSpec);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		try {
			Paint paint = new Paint();
			Bitmap bitmap = bitmaps[index];
			int imageleft = (viewWidth - bitmap.getWidth()) / 2;
			int imageTop = (viewHeight - bitmap.getHeight()) / 2;
			canvas.drawBitmap(bitmap, imageleft, imageTop, paint);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		viewWidth = w;
		viewHeight = h;
		LogUtil.i("AnimationView","viewWidth="+viewWidth+ ",viewHeight=" + viewHeight);

	}

	class MyRunnable implements Runnable {

		@Override
		public void run() {
			while (isRunning) {

				try {
					index++;
					if (index>=bitmaps.length)
					{
						index=0;
					}
					postInvalidate();
					Thread.currentThread().sleep(sleepTime);
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}
	}

}
