package com.hunk.abcd.extension.font;

import android.graphics.Typeface;
import android.support.annotation.NonNull;

/**
 * Typeface mapping class, put your customize typeface into this obj.
 * {@link UpdateFont} will consume it.
 * @author HunkDeng
 * @since 2016/6/27
 */
public class TypefaceMap {
    public final Typeface NORMAL;
    public final Typeface ITALIC;
    public final Typeface BOLD;
    public final Typeface BOLD_ITALIC;

    public TypefaceMap(@NonNull Typeface normal,
                       @NonNull Typeface italic,
                       @NonNull Typeface bold,
                       @NonNull Typeface bold_italic) {
        NORMAL = normal;
        ITALIC = italic;
        BOLD = bold;
        BOLD_ITALIC = bold_italic;
    }
}
