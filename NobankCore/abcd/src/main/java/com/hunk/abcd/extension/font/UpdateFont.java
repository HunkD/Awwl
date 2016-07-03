package com.hunk.abcd.extension.font;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hunk.abcd.extension.log.Logging;

/**
 * Update TextView's font by using this class.
 * TODO: Currently I didn't add logic for support span font customization.
 * @author HunkDeng
 * @since 2016/6/27
 */
public class UpdateFont {
    private static UpdateFont instance;
    private TypefaceMap mTypefaceMap;

    private UpdateFont() {}
    private synchronized static UpdateFont getInstance() {
        if (instance == null) {
            instance = new UpdateFont();
        }
        return instance;
    }

    public static void init(@NonNull TypefaceMap typefaceMap) {
        getInstance().mTypefaceMap = typefaceMap;
    }

    public static void updateFontsStyle(@NonNull ViewGroup vg) {
        if (vg.isInEditMode()) {
            return;
        }
        if (instance == null || instance.mTypefaceMap == null) {
            Logging.e("Don't forget call init() before using this method!");
        }
        for (int i = 0; i < vg.getChildCount(); i++) {
            View v = vg.getChildAt(i);
            if (v instanceof TextView) {
                setFontByConfig((TextView) v);
            } else if (v instanceof ViewGroup) {
                updateFontsStyle((ViewGroup) v);
            }
        }
    }

    private static void setFontByConfig(@NonNull TextView tv) {
        if (instance == null || instance.mTypefaceMap == null) {
            Logging.e("Don't forget call init() before using this method!");
        }
        TypefaceMap cache = instance.mTypefaceMap;
        if (tv.getTypeface() == null) { // default font won't have typeface.
            tv.setTypeface(cache.NORMAL);
            return;
        }
        int style = tv.getTypeface().getStyle();
        if (style == Typeface.BOLD) {
            tv.setTypeface(cache.BOLD);
        } else if (style == Typeface.ITALIC) {
            tv.setTypeface(cache.ITALIC);
        } else if (style == Typeface.NORMAL) {
            tv.setTypeface(cache.NORMAL);
        }
    }
}
