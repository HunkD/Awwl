package com.hunk.nobank;

import android.content.Context;

import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.extension.network.MyNetworkHandler;
import com.hunk.nobank.extension.network.NetworkHandler;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.manager.ScreenFlowManager;
import com.hunk.nobank.manager.dataBasic.ManagerListener;
import com.hunk.nobank.model.Cache;
import com.hunk.nobank.model.ImgLoadRequestPackage;
import com.hunk.nobank.util.Hmg;
import com.hunk.abcd.extension.log.Logging;
import com.hunk.nobank.util.ViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Keep reference to all managers, services and caches.
 * Only init the basic function service when launch app.
 */
public class Core {
    private static Core mCore;
    private NetworkHandler mNetworkHandler;
    private UserManager mUserManager;
    private ViewHelper.TypefaceCache mTypefaceCache;
    private static List<Cache<?>> mCacheList = new ArrayList<>();
    private ScreenFlowManager mScreenFlowManager;

    private Core(Context ctx) {
        // init font cache
        mTypefaceCache = ViewHelper.TypefaceCache.getInstance(ctx);
        // init network handler
        mNetworkHandler = new MyNetworkHandler(ctx);
        // init user manager
        mUserManager = new UserManager(ctx);
        // init screen flow manager
        mScreenFlowManager = new ScreenFlowManager();
        // init hmg
        Hmg.getInstance().init(ctx).setNetworkBridge(new Hmg.NetworkBridge() {
            @Override
            public void load(final String imgId, final Hmg.NetworkBridgeCallback callback) {
                ImgLoadRequestPackage pack = new ImgLoadRequestPackage(imgId);
                mNetworkHandler.fireRequest(new ManagerListener() {
                    @Override
                    public void success(String managerId, String messageId, Object data) {
                        RealResp<String> realResp = (RealResp<String>) data;
                        callback.success(realResp.Response);
                    }

                    @Override
                    public void failed(String managerId, String messageId, Object data) {
                        callback.fail();
                    }
                }, pack, null, null);
            }
        });
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

    public UserManager getUserManager() {
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

    public void setLoginManager(UserManager userManager) {
        this.mUserManager = userManager;
    }

    public static void registerCache(Cache<?> cache) {
        mCacheList.add(cache);
    }

    public static void clearCache() {
        for (Cache<?> cache : mCacheList) {
            cache.expire();
        }
    }

    public void setScreenFlowManager(ScreenFlowManager screenFlowManager) {
        mScreenFlowManager = screenFlowManager;
    }

    public ScreenFlowManager getScreenFlowManager() {
        return mScreenFlowManager;
    }
}
