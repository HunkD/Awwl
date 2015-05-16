package com.hunk.nobank;

import android.app.Application;

import com.hunk.nobank.core.CoreService;
import com.hunk.nobank.feature.Feature;
import com.hunk.nobank.feature.interfaces.Client;
import com.hunk.nobank.feature.login.manager.LoginManager;
import com.hunk.nobank.util.Hunk;
import com.hunk.nobank.util.Logging;
import com.hunk.nobank.util.ViewHelper;
import com.hunk.stubserver.StubClient;

import java.util.UUID;

public class NoBankApplication extends Application {
	
	private static NoBankApplication mInstance;
    private Client mClient;
    private ViewHelper.TypefaceCache mTypefaceCache;

    @Override
	public void onCreate() {			
		super.onCreate();
        // Set Logging TAG
        // Put a uuid into it, so we can track the error when application crash.
        Logging.TAG = getPackageName() + "[" + UUID.randomUUID().toString() + "]";

		mInstance = this;

        Hunk.HunkInfo info = Hunk.getSingInfo(this);
        Logging.i(info != null ? info.toString() : "HUNK INFO IS NULL");

        // inject fake client
        mClient = new StubClient();
        /**TODO:
         * We need make a static class to hold all components instance, since application will
         * be clean after memory cleaner runs.
         */
        mTypefaceCache = ViewHelper.TypefaceCache.getInstance(this);

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

    public ViewHelper.TypefaceCache getTypefaceCache() {
        return mTypefaceCache;
    }
}
