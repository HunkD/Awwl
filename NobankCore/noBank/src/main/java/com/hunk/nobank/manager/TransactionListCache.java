package com.hunk.nobank.manager;

import com.hunk.nobank.contract.TransactionFields;
import com.hunk.nobank.extension.network.BaseReqPackage;
import com.hunk.nobank.model.Cache;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HunkDeng
 * @since 2016/7/9
 */
public class TransactionListCache extends Cache<List<TransactionFields>> {

    @Override
    public void setCache(List<TransactionFields> cache, BaseReqPackage cachePack) {
        List<TransactionFields> _cache = new ArrayList<>();
        // set old cache
        if (get() != null && get().size() > 0) {
            _cache.addAll(get());
        }
        // change local cache parameter based on flags
        if (cache != null && cache.size() > 0) {
            _cache.addAll(cache);
        }
        super.setCache(_cache, cachePack);
    }
}