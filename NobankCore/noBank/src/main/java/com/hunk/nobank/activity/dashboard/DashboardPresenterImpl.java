package com.hunk.nobank.activity.dashboard;

import android.support.annotation.VisibleForTesting;

import com.hunk.nobank.Core;
import com.hunk.nobank.activity.base.AbstractPresenter;
import com.hunk.nobank.manager.AccountDataManager;
import com.hunk.nobank.manager.TransactionDataManager;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.manager.UserSession;
import com.hunk.nobank.manager.VaultDataManager;
import com.hunk.nobank.manager.dataBasic.ViewManagerListener;
import com.hunk.nobank.model.AccountSummaryPackage;
import com.hunk.nobank.model.TransactionReqPackage;

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

    @VisibleForTesting
    private ViewManagerListener mViewManagerListener = new ViewManagerListener(this) {
        @Override
        public void onSuccess(String managerId, String messageId, Object data) {
            if (managerId.equals(UserManager.MANAGER_ID)) {
                if (messageId.equals(UserManager.METHOD_ACCOUNT_SUMMARY)) {
                    if (UserManager.isPostLogin(mUserManager)) { // TODO: AOC
                        mAccountDataManager = mUserManager.getCurrentUserSession().getAccountDataManager();
                        mVaultDataManager = mUserManager.getCurrentUserSession().getVaultDataManager();

                        mView.showBalance(mAccountDataManager.getAccountModel().Balance);
                    } else {
                        // TODO: throw exception in debug mode, or clean listener after session expire.
                    }
                }
            } else if (managerId.equals(TransactionDataManager.MANAGER_ID)) {
                if (messageId.equals(TransactionDataManager.METHOD_TRANSACTION)) {
                    mView.showTransactionList(TransactionReqPackage.cache.get().Response);
                }
            }
        }

        @Override
        public void onFailed(String managerId, String messageId, Object data) {

        }
    };

    public DashboardPresenterImpl() {
        mUserManager = Core.getInstance().getUserManager();
        mUserManager.registerViewManagerListener(mViewManagerListener);
        UserSession currentUserSession = mUserManager.getCurrentUserSession();
        // when we recreate this application from lowMemoryKiller, it will be session timeout state
        if (currentUserSession != null) {
            mTransactionDataManager = currentUserSession.getTransactionDataManager();
            mTransactionDataManager.registerViewManagerListener(mViewManagerListener);
        }
    }

    @Override
    public void detach() {
        super.detach();
        // TODO: remove this
        mUserManager.unregisterViewManagerListener(mViewManagerListener);
        if (mTransactionDataManager != null) {
            mTransactionDataManager.unregisterViewManagerListener(mViewManagerListener);
        }
    }

    @Override
    public void onResume() {
        if (mUserManager.fetchAccountSummary(new AccountSummaryPackage(), mViewManagerListener)) {
            mView.showLoadingBalance();
        }
    }

    @Override
    public void forceRefreshAction() {
        TransactionReqPackage.cache.expire();
        if (mTransactionDataManager != null) {
            mTransactionDataManager.fetchTransactions(false, mViewManagerListener);
        }
    }

    @Override
    public void firstTimeResume() {
        forceRefreshAction();
    }

    public void showMoreTransactionsAction() {
        mTransactionDataManager.fetchTransactions(true, mViewManagerListener);
    }
}
