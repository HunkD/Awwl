package com.hunk.nobank.manager.dataBasic;

import com.hunk.nobank.Core;
import com.hunk.nobank.extension.network.BaseReqPackage;
import com.hunk.nobank.model.Cache;

import rx.Observable;

public abstract class DataManager {
    /**
     *
     * @return
     *  fetch from network or not
     */
    public <T> Observable<T> invokeNetwork(final Cache<T> cache, final BaseReqPackage baseReqPackage) {
        if (cache.shouldFetch(baseReqPackage)) {
            return Core.getInstance().getNetworkHandler().fireRequest(baseReqPackage);
        } else {
            return Observable.just(cache.get());
        }
    }
}
