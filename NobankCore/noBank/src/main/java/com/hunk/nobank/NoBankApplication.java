package com.hunk.nobank;

import android.app.Application;

import com.hunk.nobank.feature.interfaces.Client;
import com.hunk.nobank.util.Logging;

public class NoBankApplication extends Application {
	
	private static NoBankApplication mInstance;
	@Override
	public void onCreate() {			
		super.onCreate();
        // Set Logging TAG
        Logging.TAG = getPackageName();

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
