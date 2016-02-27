package com.hunk.nobank.manager;

import com.hunk.nobank.Core;
import com.hunk.nobank.contract.AccountSummary;
import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.contract.TransactionFields;
import com.hunk.nobank.extension.network.BaseReqPackage;
import com.hunk.nobank.manager.dataBasic.DataManager;
import com.hunk.nobank.manager.dataBasic.ManagerListener;
import com.hunk.nobank.model.Cache;
import com.hunk.nobank.model.TransactionReqPackage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class TransactionDataManager extends DataManager {

    private static final String MANAGER_ID = "TransactionDataManager";
    private final AccountSummary mAccountSummary;

    public TransactionDataManager(AccountSummary accountSummary) {
        this.mAccountSummary = accountSummary;

        // Change cache with custom logic
        TransactionReqPackage.cache = new TransactionListCache();
    }

    public static final String METHOD_TRANSACTION = "METHOD_TRANSACTION";

    /**
     *
     * @param more
     * @param managerListener
     * @return
     *  Whether app call network request
     */
    public boolean fetchTransactions(boolean more, ManagerListener managerListener) {
        TransactionListCache cache = (TransactionListCache) TransactionReqPackage.cache;
        cache.setMore(more);

        long timestamp = more ?
                cache.get().Response.get(cache.get().Response.size() - 1).getTimeStamp() : new Date().getTime();

        TransactionReqPackage transactionReqPackage = new TransactionReqPackage(mAccountSummary, timestamp);
        if (more || TransactionReqPackage.cache.shouldFetch(transactionReqPackage)) {
            Core.getInstance().getNetworkHandler()
                    .fireRequest(managerListener, transactionReqPackage,
                            getManagerId(), METHOD_TRANSACTION);
            return true;
        } else {
            managerListener.success(getManagerId(), METHOD_TRANSACTION, TransactionReqPackage.cache.get());
            return false;
        }
    }

    public final String getManagerId() {
        return MANAGER_ID;
    }

    private class TransactionListCache extends Cache<RealResp<List<TransactionFields>>> {

        private boolean mMore;

        public void setMore(boolean more) {
            mMore = more;
        }

        @Override
        public void setCache(RealResp<List<TransactionFields>> cache, BaseReqPackage cachePack) {
            // change local cache parameter based on flags
            if (mMore && get() != null && get().Response != null && cache.Response != null) {
                List<TransactionFields> oldCache = new ArrayList<>(get().Response);
                oldCache.addAll(cache.Response);
                cache.Response = oldCache;
                // reset in case next time cache.
                mMore = false;
            }
            super.setCache(cache, cachePack);
        }
    }
}
