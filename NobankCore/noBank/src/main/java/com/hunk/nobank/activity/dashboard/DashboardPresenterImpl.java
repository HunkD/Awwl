package com.hunk.nobank.activity.dashboard;

import com.hunk.abcd.activity.mvp.AbstractPresenter;
import com.hunk.nobank.Core;
import com.hunk.nobank.activity.BaseViewObserver;
import com.hunk.nobank.contract.AccountSummary;
import com.hunk.nobank.contract.TransactionFields;
import com.hunk.nobank.manager.AccountDataManager;
import com.hunk.nobank.manager.TransactionDataManager;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.manager.UserSession;
import com.hunk.nobank.manager.VaultDataManager;
import com.hunk.nobank.model.TransactionReqPackage;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;

/**
 * @author HunkDeng
 * @since 2016/5/23
 */
public class DashboardPresenterImpl
        extends AbstractPresenter<DashboardView>
        implements DashboardPresenter<DashboardView> {

    private UserManager mUserManager;
    private TransactionDataManager mTransactionDataManager;
    private AccountDataManager mAccountDataManager;
    private VaultDataManager mVaultDataManager;

    public DashboardPresenterImpl() {
        mUserManager = Core.getInstance().getUserManager();
        UserSession currentUserSession = mUserManager.getCurrentUserSession();
        // when we recreate this application from lowMemoryKiller, it will be session timeout state
        if (currentUserSession != null) {
            mTransactionDataManager = currentUserSession.getTransactionDataManager();
        }
    }

    @Override
    public void detach() {
        super.detach();
    }

    @Override
    public void onResume() {
        mUserManager
                .fetchAccountSummary()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseViewObserver<AccountSummary>(this) {
                    @Override
                    public void onNext(AccountSummary accountSummary) {
                        if (UserManager.isPostLogin(mUserManager)) { // TODO: AOC
                            mAccountDataManager = mUserManager.getCurrentUserSession().getAccountDataManager();
                            mVaultDataManager = mUserManager.getCurrentUserSession().getVaultDataManager();

                            mView.showBalance(mAccountDataManager.getAccountModel().Balance);
                        } else {
                            // TODO: throw exception in debug mode, or clean listener after session expire.
                        }
                    }
                });
        mView.showLoadingBalance();
    }

    @Override
    public void forceRefreshAction() {
        if (mTransactionDataManager != null) {
            mTransactionDataManager.fetchTransactions(false)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseViewObserver<List<TransactionFields>>(this) {
                        @Override
                        public void onNext(List<TransactionFields> transactionFieldsList) {
                            mView.showTransactionList(TransactionReqPackage.cache.get());
                        }
                    });
        }
    }

    @Override
    public void firstTimeResume() {
        forceRefreshAction();
    }

    public void showMoreTransactionsAction() {
        mTransactionDataManager.fetchTransactions(true)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseViewObserver<List<TransactionFields>>(this) {
                    @Override
                    public void onNext(List<TransactionFields> transactionFieldsList) {
                        mView.showTransactionList(TransactionReqPackage.cache.get());
                    }
                });
    }
}
