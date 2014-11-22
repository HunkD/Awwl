package com.hunk.nobank.feature.base.activity;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Description :<br>
 * Provides Application Preference Object
 */
public class BaseFeaturePreference {
	
	private static final String APP_SHARED_PREFERENCES_NAME = "NobankSharedPref";

	private static final String KEY_IS_REMEMBER_ME = "KEY_IS_REMEMBER_ME";
	private static final String KEY_IS_REMEMBER_ME_USERNAME = "KEY_IS_REMEMBER_ME_USERNAME";
		
	private Context mCtx;
	
	public BaseFeaturePreference(Context ctx) {
		this.mCtx = ctx;
	}
	
	public boolean isRememberMe() {
		SharedPreferences pref = getSharedPreferences();
		return pref.getBoolean(KEY_IS_REMEMBER_ME, false);
	}
	
	public String getRememberMeUserName() {
		SharedPreferences pref = getSharedPreferences();
		return pref.getString(KEY_IS_REMEMBER_ME_USERNAME, null);
	}
	
	public void setRememberMe(boolean isRememberMe, String rememberMeUserName) {
		SharedPreferences pref = getSharedPreferences();
		pref.edit()
		.putBoolean(KEY_IS_REMEMBER_ME, isRememberMe)
		.putString(KEY_IS_REMEMBER_ME_USERNAME, rememberMeUserName)
		.commit();
	}
	
	public SharedPreferences getSharedPreferences() {
		return mCtx.getSharedPreferences(APP_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
	}
}
