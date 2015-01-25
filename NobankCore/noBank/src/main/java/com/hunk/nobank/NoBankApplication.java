package com.hunk.nobank;

import android.app.Application;

import com.hunk.nobank.feature.interfaces.Client;

public class NoBankApplication extends Application {
	
	private static NoBankApplication mInstance;
	@Override
	public void onCreate() {			
		super.onCreate();
		mInstance = this;
	}

	public static NoBankApplication getInstance() {
		return mInstance;
	}

	public Client getClient() {
		// TODO Auto-generated method stub
		return null;
	}
}
