package com.hunk.nobank.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author HunkDeng
 * @since 2016/5/7
 */
public class TimeUtils {
    final static SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyyMMdd HH:mm:ss", Locale.US);
    public static final String PST = "PST";
    public static final String UTC = "UTC";
    public static final String UTC8 = "UTC+8";

    public static String formatTimeString(Date time) {
        return dateFormat.format(time);
    }

    public static String formatTimeString(Date time, TimeZone timeZone) {
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyyMMdd HH:mm:ss", Locale.US);
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(time);
    }
}
