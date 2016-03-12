package com.tarena.allrun.view;

import com.tarena.allrun.R;
import com.tarena.allrun.util.LogUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class AnimationView extends View {
	Bitmap[] bitmaps = null;
	int sleepTime = 100;
	int currentIndex = 0;
	int viewWidth, viewHeight;
	public boolean isRunning = true;
	Thread thread;

	public AnimationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.animation);
		sleepTime = (int) ta.getFloat(R.styleable.animation_sleep_time, 100);

		TypedArray taImage = context.getResources().obtainTypedArray(
				R.array.animationImages);
		int length = taImage.length();
		bitmaps = new Bitmap[length];
		for (int i = 0; i < length; i++) {
			int imageId = taImage.getResourceId(i, 1);
			bitmaps[i] = BitmapFactory.decodeResource(getResources(), imageId);
		}

		thread = new Thread(new MyRunnable());
		thread.start();
		ta.recycle();

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int mode = MeasureSpec.getMode(widthMeasureSpec);
		if (mode == MeasureSpec.AT_MOST) {
			int measuredWidth = bitmaps[0].getWidth();
			int measuredHeight = bitmaps[0].getHeight();
			LogUtil.i("测量", measuredWidth + "," + measuredHeight);
			setMeasuredDimension(measuredWidth, measuredHeight);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		Bitmap bitmap = bitmaps[currentIndex];
		int x = (viewWidth - bitmap.getWidth()) / 2;
		int y = (viewHeight - bitmap.getHeight()) / 2;
		canvas.drawBitmap(bitmap, x, y, paint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		viewHeight = h;
		viewWidth = w;
	}

	class MyRunnable implements Runnable {

		@Override
		public void run() {
			while (isRunning) {

				try {
					currentIndex++;
					if (currentIndex >= bitmaps.length) {
						currentIndex = 0;
					}
					postInvalidate();
					Thread.currentThread().sleep(sleepTime);
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		LogUtil.i("事件", "AnimationView dispatchTouchEvent " + ev.getAction());
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		LogUtil.i("事件", "AnimationView onTouchEvent " + event.getAction());
		 return super.onTouchEvent(event);
		//return true;
	}
}
