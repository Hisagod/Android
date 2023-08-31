package com.opensource.svgaplayer.utils;

import android.os.Build;
import android.text.TextUtils;
import android.view.View;

import com.opensource.svgaplayer.SVGA;

import java.util.Locale;

public class ViewUtils {
    public static boolean isLayoutRtl() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Locale primaryLocale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                primaryLocale = SVGA.app.getResources().getConfiguration().getLocales().get(0);
            } else {
                primaryLocale = SVGA.app.getResources().getConfiguration().locale;
            }
            return TextUtils.getLayoutDirectionFromLocale(primaryLocale) == View.LAYOUT_DIRECTION_RTL;
        }
        return false;
    }
}
