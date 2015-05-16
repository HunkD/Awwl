package com.hunk.nobank;

import android.content.Context;

import com.hunk.nobank.extension.network.interfaces.Client;
import com.hunk.nobank.manager.LoginManager;
import com.hunk.nobank.util.Logging;
import com.hunk.nobank.util.ViewHelper;
import com.hunk.stubserver.StubClient;

/**
 * Keep reference to all managers, services and caches.
 */
public class Core {
    private static Core mCore;
    private LoginManager mLoginManager;
    private Client mClient;
    private ViewHelper.TypefaceCache mTypefaceCache;

    private Core(Context ctx) {

        mLoginManager = new LoginManager(ctx);
        mTypefaceCache = ViewHelper.TypefaceCache.getInstance(ctx);

        // inject fake client
        mClient = new StubClient();
    }

    public synchronized static Core getInstance() {
        if (mCore == null) {
            Logging.w("async thread has touched core class!");
        }
        return mCore;
    }

    public static void start(Context ctx) {
        mCore = new Core(ctx);
    }

    public LoginManager getLoginManager() {
        return mLoginManager;
    }


    public Client getClient() {
        return mClient;
    }

    public ViewHelper.TypefaceCache getTypefaceCache() {
        return mTypefaceCache;
    }
}
