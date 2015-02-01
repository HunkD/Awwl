package com.hunk.nobank.util;

import android.util.Log;

public class Logging {
	public static String TAG = "HUNK";
	
	private static enum Level {
		DEBUG,
		WARNING,
		ERROR,
		INFO
	}
	
	public static void d(String message) {
		doLog(Level.DEBUG, TAG, message);
	}
	
	public static void w(String message) {
		doLog(Level.WARNING, TAG, message);
	}
	
	public static void e(String message) {
		doLog(Level.ERROR, TAG, message);
	}
	
	public static void i(String message) {
		doLog(Level.INFO, TAG, message);
	}
	
	private static void doLog(Level level, String TAG, String message) {
		switch (level) {
		case DEBUG:
			Log.d(TAG, message);
			break;
		case WARNING:
			Log.w(TAG, message);
			break;
		case ERROR:
			Log.e(TAG, message);
			break;
		case INFO:
			Log.i(TAG, message);
		default: 
			break;
		}
	}
}
