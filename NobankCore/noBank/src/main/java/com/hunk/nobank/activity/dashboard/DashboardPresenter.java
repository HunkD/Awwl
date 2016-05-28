package com.hunk.nobank.activity.dashboard;

import android.support.annotation.VisibleForTesting;
import android.view.View;

import com.hunk.nobank.Core;
import com.hunk.nobank.activity.dashboard.transaction.LoadingState;
import com.hunk.nobank.activity.dashboard.transaction.ViewTransactionFields;
import com.hunk.nobank.contract.AccountType;
import com.hunk.nobank.manager.AccountDataManager;
import com.hunk.nobank.manager.TransactionDataManager;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.manager.UserSession;
import com.hunk.nobank.manager.dataBasic.ViewManagerListener;
import com.hunk.nobank.model.AccountSummaryPackage;
import com.hunk.nobank.model.TransactionReqPackage;
import com.hunk.nobank.views.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HunkDeng
 * @since 2016/5/23
 */
public class DashboardPresenter {

    @VisibleForTesting
    private DashboardView mView;

    private UserManager mUserManager;
    private TransactionDataManager mTransactionDataManager;
    private AccountDataManager mMainAccountDataManager;
    private AccountDataManager mVaultAccountDataManager;

    @VisibleForTesting
    private ViewManagerListener mViewManagerListener = new ViewManagerListener(this) {
        @Override
        public void onSuccess(String managerId, String messageId, Object data) {
            if (managerId.equals(UserManager.MANAGER_ID)) {
                if (messageId.equals(UserManager.METHOD_ACCOUNT_SUMMARY)) {
                    if (UserManager.isPostLogin(mUserManager)) { // TODO: AOC
                        mMainAccountDataManager = mUserManager.getCurrentUserSession().getAccountDataManagerByType(AccountType.Main);
                        mVaultAccountDataManager = mUserManager.getCurrentUserSession().getAccountDataManagerByType(AccountType.Vault);

                        mView.showBalance(mMainAccountDataManager.getAccountModel().Balance);
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

    public DashboardPresenter(DashboardView view) {
        mView = view;

        mUserManager = Core.getInstance().getUserManager();
        mUserManager.registerViewManagerListener(mViewManagerListener);
        UserSession currentUserSession = mUserManager.getCurrentUserSession();
        // when we recreate this application from lowMemoryKiller, it will be session timeout state
        if (currentUserSession != null) {
            mTransactionDataManager = currentUserSession.getTransactionDataManager();
            mTransactionDataManager.registerViewManagerListener(mViewManagerListener);
        }
    }


    public void onDestroy() {
        mView = null;
        // TODO: remove this
        mUserManager.unregisterViewManagerListener(mViewManagerListener);
        if (mTransactionDataManager != null) {
            mTransactionDataManager.unregisterViewManagerListener(mViewManagerListener);
        }
    }

    public void onResume() {
        if (mUserManager.fetchAccountSummary(new AccountSummaryPackage(), mViewManagerListener)) {
            mView.showLoadingBalance();
        }
    }

    public void forceRefreshAction() {
        TransactionReqPackage.cache.expire();
        if (mTransactionDataManager != null) {
            mTransactionDataManager.fetchTransactions(false, mViewManagerListener);
        }
    }

    public void firstTimeResume() {
        forceRefreshAction();
    }

    public void showMoreTransactionsAction() {
        mTransactionDataManager.fetchTransactions(true, mViewManagerListener);
    }
}
