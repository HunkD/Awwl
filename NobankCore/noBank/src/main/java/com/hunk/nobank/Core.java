package com.hunk.nobank;

import android.content.Context;
import android.graphics.Typeface;

import com.hunk.abcd.activity.flow.ScreenFlowManager;
import com.hunk.abcd.extension.font.TypefaceMap;
import com.hunk.abcd.extension.font.UpdateFont;
import com.hunk.abcd.extension.log.Logging;
import com.hunk.nobank.extension.network.MyNetworkHandler;
import com.hunk.nobank.extension.network.NetworkHandler;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.model.Cache;
import com.hunk.nobank.model.ImgLoadRequestPackage;
import com.hunk.nobank.util.Hmg;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Keep reference to all managers, services and caches.
 * Only init the basic function service when launch app.
 */
public class Core {
    private static Core mCore;
    private NetworkHandler mNetworkHandler;
    private UserManager mUserManager;
    private static List<Cache<?>> mCacheList = new ArrayList<>();
    private ScreenFlowManager mScreenFlowManager;

    private Core(Context ctx) {
        // init UpdateFont
        UpdateFont.init(
                new TypefaceMap(
                        Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto-Light.ttf"),
                        Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto-LightItalic.ttf"),
                        Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto-Regular.ttf"),
                        Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto-Light.ttf")));
        // init network handler
        mNetworkHandler = new MyNetworkHandler();
        // init user manager
        mUserManager = new UserManager(ctx);
        // init screen flow manager
        mScreenFlowManager = new ScreenFlowManager();

        // init hmg
        Hmg.getInstance().init(ctx).setNetworkBridge(new Hmg.NetworkBridge() {
            @Override
            public void load(final String imgId, final Hmg.NetworkBridgeCallback callback) {
                ImgLoadRequestPackage pack = new ImgLoadRequestPackage(imgId);
                mNetworkHandler
                        .fireRequest(pack)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Object>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                callback.fail();
                            }

                            @Override
                            public void onNext(Object s) {
                                callback.success((String) s);
                            }
                        });
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
