package com.hunk.nobank;

import android.app.Application;

import com.hunk.nobank.feature.interfaces.Client;
import com.hunk.nobank.util.Hunk;
import com.hunk.nobank.util.Logging;

import java.util.UUID;

public class NoBankApplication extends Application {
	
	private static NoBankApplication mInstance;
	@Override
	public void onCreate() {			
		super.onCreate();
        // Set Logging TAG
        // Put a uuid into it, so we can track the error when application crash.
        Logging.TAG = getPackageName() + "[" + UUID.randomUUID().toString() + "]";

		mInstance = this;
        Hunk.HunkInfo info = Hunk.getSingInfo(this);
        Logging.i(info.toString());
    }

	public static NoBankApplication getInstance() {
		return mInstance;
	}

	public Client getClient() {
		// TODO Auto-generated method stub
		return null;
	}
}
