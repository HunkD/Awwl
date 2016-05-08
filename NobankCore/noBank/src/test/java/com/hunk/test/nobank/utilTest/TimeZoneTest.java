package com.hunk.test.nobank.utilTest;

import com.hunk.nobank.util.TimeUtils;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author HunkDeng
 * @since 2016/5/7
 */
public class TimeZoneTest {
    @Test
    public void FormatUTCTime() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(TimeUtils.UTC8));
        calendar.setTime(date);

        System.out.println(
                TimeUtils.formatTimeString(calendar.getTime()));
        System.out.println(
                TimeUtils.formatTimeString(calendar.getTime(), TimeZone.getTimeZone(TimeUtils.UTC)));

        calendar.setTimeZone(TimeZone.getTimeZone(TimeUtils.UTC8));
        calendar.add(Calendar.DAY_OF_MONTH, -120);
        System.out.println(
                TimeUtils.formatTimeString(calendar.getTime()));
        System.out.println(
                TimeUtils.formatTimeString(calendar.getTime(), TimeZone.getTimeZone(TimeUtils.UTC)));
    }

    @Test
    public void FormatUTCTime2() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(TimeUtils.PST));
        calendar.setTime(date);

        System.out.println(
                TimeUtils.formatTimeString(calendar.getTime()));
        System.out.println(
                TimeUtils.formatTimeString(calendar.getTime(), TimeZone.getTimeZone(TimeUtils.UTC)));

        calendar.setTimeZone(TimeZone.getTimeZone(TimeUtils.PST));
        calendar.add(Calendar.DAY_OF_MONTH, -120);
        System.out.println(
                TimeUtils.formatTimeString(calendar.getTime()));
        System.out.println(
                TimeUtils.formatTimeString(calendar.getTime(), TimeZone.getTimeZone(TimeUtils.UTC)));
    }
}
