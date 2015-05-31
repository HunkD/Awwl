package com.hunk.nobank;

import android.content.Context;

import com.hunk.nobank.extension.network.MyNetworkHandler;
import com.hunk.nobank.extension.network.NetworkHandler;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.util.Logging;
import com.hunk.nobank.util.ViewHelper;

/**
 * Keep reference to all managers, services and caches.
 */
public class Core {
    private static Core mCore;
    private NetworkHandler mNetworkHandler;
    private UserManager mUserManager;
    private ViewHelper.TypefaceCache mTypefaceCache;

    private Core(Context ctx) {
        mTypefaceCache = ViewHelper.TypefaceCache.getInstance(ctx);

        mNetworkHandler = new MyNetworkHandler(ctx);

        mUserManager = new UserManager(ctx);
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

    public UserManager getLoginManager() {
        return mUserManager;
    }

    public ViewHelper.TypefaceCache getTypefaceCache() {
        return mTypefaceCache;
    }

    public NetworkHandler getNetworkHandler() {
        return mNetworkHandler;
    }

    public void setNetworkHandler(NetworkHandler networkHandler) {
        this.mNetworkHandler = networkHandler;
    }
}
