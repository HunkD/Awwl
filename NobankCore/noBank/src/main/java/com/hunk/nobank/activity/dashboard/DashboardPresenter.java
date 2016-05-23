package com.hunk.nobank.activity.dashboard;

import com.hunk.nobank.Core;
import com.hunk.nobank.contract.AccountType;
import com.hunk.nobank.manager.AccountDataManager;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.manager.dataBasic.ViewManagerListener;
import com.hunk.nobank.model.AccountSummaryPackage;

/**
 * @author HunkDeng
 * @since 2016/5/23
 */
public class DashboardPresenter {
    private DashboardView mView;

    private UserManager mUserManager;
    private AccountDataManager mMainAccountDataManager;
    private AccountDataManager mVaultAccountDataManager;

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
    }


    public void onDestroy() {
        mView = null;
        // TODO: remove this
        mUserManager.unregisterViewManagerListener(mViewManagerListener);
    }

    public void onResume() {
        if (mUserManager.fetchAccountSummary(new AccountSummaryPackage(), mViewManagerListener)) {
            mView.showLoadingBalance();
        }
    }
}
