package com.hunk.nobank.model;

import com.hunk.nobank.extension.network.BaseReqPackage;

/**
 *
 */
public interface Cacheable<T> {
    void setCache(T realResp, BaseReqPackage req);
}
