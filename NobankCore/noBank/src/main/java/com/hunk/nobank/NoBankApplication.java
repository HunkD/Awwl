package com.hunk.nobank;

import android.app.Application;

import com.hunk.nobank.core.CoreService;
import com.hunk.nobank.feature.Feature;
import com.hunk.nobank.feature.interfaces.Client;
import com.hunk.nobank.feature.login.manager.LoginManager;
import com.hunk.nobank.util.Hunk;
import com.hunk.nobank.util.Logging;
import com.hunk.stubserver.StubClient;

import java.util.UUID;

public class NoBankApplication extends Application {
	
	private static NoBankApplication mInstance;
    private Client mClient;

    @Override
	public void onCreate() {			
		super.onCreate();
        // Set Logging TAG
        // Put a uuid into it, so we can track the error when application crash.
        Logging.TAG = getPackageName() + "[" + UUID.randomUUID().toString() + "]";

		mInstance = this;
        Hunk.HunkInfo info = Hunk.getSingInfo(this);
        Logging.i(info.toString());

        // inject fake client
        mClient = new StubClient();

        // mapping feature
        CoreService.mRegisteredFeatureManager.put(
                Feature.login.toString(), new LoginManager(getApplicationContext()));
    }

	public static NoBankApplication getInstance() {
		return mInstance;
	}

	public Client getClient() {
		return mClient;
	}
}
