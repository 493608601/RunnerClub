package com.tarena.allrun.util;

import android.content.Context;

public class DisplayUtil {
	public static int sp2px(Context context, int sp) {
		// metric ∂»¡ø
		// density √‹∂»
		float desity = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (sp * desity + 0.5f);
	}
}
