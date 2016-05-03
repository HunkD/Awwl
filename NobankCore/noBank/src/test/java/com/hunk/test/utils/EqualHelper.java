package com.hunk.test.utils;

import android.content.Intent;

import static org.junit.Assert.assertEquals;

/**
 * @author HunkDeng
 * @since 2016/5/3
 */
public class EqualHelper {
    public static void assertIntentEquals(Intent expected, Intent actual) {
        assertEquals(expected.getPackage(), actual.getPackage());
        assertEquals(expected.getAction(), actual.getAction());
        assertEquals(expected.getFlags(), actual.getFlags());
        assertEquals(expected.getComponent(), actual.getComponent());
    }
}
