package com.lowett.core.utils;


import android.app.Activity;
import android.util.DisplayMetrics;

import com.lowett.core.Frame;


public final class DisplayUtil {


    public static int getScreenWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }


    public static int dip2px(int dipValue) {
        float reSize = Frame.getInstance().getAppContext().getResources().getDisplayMetrics().density;
        return (int) ((dipValue * reSize) + 0.5);
    }


    public static int px2dip(int pxValue) {
        float reSize = Frame.getInstance().getAppContext().getResources().getDisplayMetrics().density;
        return (int) ((pxValue / reSize) + 0.5);
    }

    // 不同分辨率下字体大小的设置
    public static int spToSize(int dpValue) {
        DisplayMetrics dm = Frame.getInstance().getAppContext().getResources().getDisplayMetrics();
        return (int) (dpValue * dm.density);
    }


    public static float getDensity() {
    	return Frame.getInstance().getAppContext().getResources().getDisplayMetrics().density;
    }

    public static float getScaledDensity() {
       return Frame.getInstance().getAppContext().getResources().getDisplayMetrics().scaledDensity;
    }


    public static int getScaleSize(int sizePx) {
        return (int) (sizePx * DisplayUtil.getScaledDensity());
    }
}
