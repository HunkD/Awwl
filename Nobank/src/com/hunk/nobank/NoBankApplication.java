package com.hunk.nobank;

import android.app.Application;

import com.hunk.nobank.feature.base.activity.BaseFeaturePreference;
import com.hunk.nobank.feature.interfaces.Client;

public class NoBankApplication extends Application {
	
	BaseFeaturePreference mAppPreference;
	boolean mIsSignin;
	
	private static NoBankApplication mInstance;
	@Override
	public void onCreate() {			
		super.onCreate();
		mInstance = this;
		mAppPreference = new BaseFeaturePreference(this);		
	}
	
	public BaseFeaturePreference getAppPreference() {
		return mAppPreference;
	}

	public static NoBankApplication getInstance() {
		return mInstance;
	}

	public Client getClient() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean isSignIn() {
		return mIsSignin;
	}
}
