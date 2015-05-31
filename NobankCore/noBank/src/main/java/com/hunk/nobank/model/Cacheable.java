package com.hunk.nobank.model;

import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.extension.network.BaseReqPackage;

/**
 *
 */
public interface Cacheable {
    void setCache(RealResp realResp, BaseReqPackage req);
}
