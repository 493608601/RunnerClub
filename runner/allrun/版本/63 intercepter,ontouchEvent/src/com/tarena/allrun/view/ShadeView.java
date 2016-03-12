package com.tarena.allrun.view;

import com.tarena.allrun.R;
import com.tarena.allrun.util.DisplayUtil;
import com.tarena.allrun.util.LogUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class ShadeView extends View {
	String text;
	int text_color, shade_color,text_size;
	int Stringheight,StringWidth;
	
	public ShadeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray ta = context
				.obtainStyledAttributes(attrs, R.styleable.Shade);
		text = ta.getString(R.styleable.Shade_text);
		text_color = ta.getColor(R.styleable.Shade_text_color, 0);
		shade_color = ta.getColor(R.styleable.Shade_shade_color, 0);
		text_size=(int) ta.getFloat(R.styleable.Shade_text_size, 12);
		text_size=DisplayUtil.sp2px(getContext(), text_size);
	}
//执行在onDraw之前
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Paint paint = new Paint();
		paint.setTextSize(text_size);
		Rect rect=new Rect();
		paint.getTextBounds(text, 0, text.length(), rect);
		 StringWidth=rect.width();//101
		 Stringheight=rect.height();//12
		
		int w2=(int) paint.measureText(text);
		LogUtil.i("ShadeView", "StringWidth="+StringWidth+",Stringheight="+Stringheight+",w2="+w2);
		int mode=MeasureSpec.getMode(widthMeasureSpec);
		if (mode==MeasureSpec.AT_MOST)
		{
			//setMeasuredDimension(1080, 500);
			//setMeasuredDimension(101, 500);
			//12,120不显示文字
			setMeasuredDimension(StringWidth+text_size/20, Stringheight+text_size/20+10);



		}
		
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// String text=this.getText().toString();
		// int color=this.getTextColors().getDefaultColor();
		try {
			Paint paint = new Paint();
			paint.setTextSize(text_size);
			paint.setColor(shade_color);
			int x = (int) this.getX();
			int y = (int) this.getY();
			canvas.drawText(text,  text_size/20, Stringheight+text_size/20, paint);

			paint.setColor(text_color);
			canvas.drawText(text, 0, Stringheight, paint);

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
