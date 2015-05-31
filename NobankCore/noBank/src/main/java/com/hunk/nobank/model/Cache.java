package com.hunk.nobank.model;

import com.hunk.nobank.extension.network.BaseReqPackage;

/**
 *
 */
public class Cache<Resp> {
    private static final long SHORT_TIMEOUT = 60 * 1000;
    private static final long LONG_TIMEOUT = 5 * 60 * 1000;
    private Resp cache;
    private long cacheTimestamp;
    private long cacheTimeout;
    private BaseReqPackage cachePack;

    public Cache() {
        this(SHORT_TIMEOUT);
    }

    public Cache(long cacheTimeout) {
        this.cacheTimeout = cacheTimeout;
    }

    public void setCache(Resp cache, BaseReqPackage cachePack) {
        cacheTimestamp = System.currentTimeMillis();
        this.cache = cache;
        this.cachePack = cachePack;
    }

    public void expire() {
        cache = null;
    }

    public Resp get() {
        return cache;
    }

    public boolean shouldFetch(BaseReqPackage pack) {
        return cache == null
                || System.currentTimeMillis() - cacheTimestamp > cacheTimeout
                || !pack.requestEquals(cachePack);
    }
}
