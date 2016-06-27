package com.hunk.nobank;

import android.app.Application;

import com.hunk.abcd.extension.sign.Hunk;
import com.hunk.abcd.extension.log.Logging;
import com.hunk.abcd.extension.util.ViewHelper;
import com.squareup.leakcanary.LeakCanary;

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
        Logging.i(info != null ? info.toString() : "HUNK INFO IS NULL");

        Core.start(this.getApplicationContext());

        if (NBuildConstants.DEBUG) {
            LeakCanary.install(this);
        }
    }

    public static NoBankApplication getInstance() {
        return mInstance;
    }

}
