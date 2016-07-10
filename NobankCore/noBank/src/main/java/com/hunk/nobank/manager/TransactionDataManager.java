package com.hunk.nobank.manager;

import com.hunk.nobank.Core;
import com.hunk.nobank.contract.AccountSummary;
import com.hunk.nobank.contract.TransactionFields;
import com.hunk.nobank.manager.dataBasic.DataManager;
import com.hunk.nobank.model.TransactionReqPackage;

import java.util.Date;
import java.util.List;

import rx.Observable;

/**
 *
 */
public class TransactionDataManager extends DataManager {
    private final AccountSummary mAccountSummary;

    public TransactionDataManager(AccountSummary accountSummary) {
        this.mAccountSummary = accountSummary;
    }

    /**
     *
     * @param more
     * @return
     *  Whether app call network request
     */
    public Observable<List<TransactionFields>> fetchTransactions(boolean more) {
        TransactionListCache cache = TransactionReqPackage.cache;

        long timestamp = more ?
                cache.get().get(cache.get().size() - 1).getTimeStamp() : new Date().getTime();

        TransactionReqPackage transactionReqPackage = new TransactionReqPackage(mAccountSummary, timestamp);
        if (more || TransactionReqPackage.cache.shouldFetch(transactionReqPackage)) {
            return Core.getInstance().getNetworkHandler().fireRequest(transactionReqPackage);
        } else {
            return Observable.just(cache.get());
        }
    }
}
