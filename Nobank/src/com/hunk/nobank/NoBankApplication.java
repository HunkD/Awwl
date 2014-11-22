package com.hunk.nobank;

import android.app.Application;

import com.hunk.nobank.feature.base.activity.BaseFeaturePreference;

public class NoBankApplication extends Application {
	BaseFeaturePreference mAppPreference;
	@Override
	public void onCreate() {			
		super.onCreate();
		mAppPreference = new BaseFeaturePreference(this);
	}

	public boolean isSignIn() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public BaseFeaturePreference getAppPreference() {
		return mAppPreference;
	}

	public Object getFeatureManager(String account) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
