package com.hunk.nobank.manager;

import com.hunk.nobank.Core;
import com.hunk.nobank.contract.AccountSummary;
import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.contract.TransactionFields;
import com.hunk.nobank.model.Cache;
import com.hunk.nobank.model.TransactionReqPackage;

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
    }

    public static final String METHOD_TRANSACTION = "METHOD_TRANSACTION";
    public void fetchTransactions(boolean more, ManagerListener managerListener) {
        Cache<RealResp<List<TransactionFields>>> cache = TransactionReqPackage.cache;
        long timestamp = more ?
                cache.get().Response.get(cache.get().Response.size() - 1).getTimeStamp() : new Date().getTime();
        TransactionReqPackage transactionReqPackage = new TransactionReqPackage(mAccountSummary, timestamp);
        if (more || TransactionReqPackage.cache.shouldFetch(transactionReqPackage)) {
            Core.getInstance().getNetworkHandler()
                    .fireRequest(managerListener, transactionReqPackage,
                            getManagerId(), METHOD_TRANSACTION);
        } else {
            managerListener.success(getManagerId(), METHOD_TRANSACTION, TransactionReqPackage.cache.get());
        }
    }

    public final String getManagerId() {
        return MANAGER_ID;
    }
}
