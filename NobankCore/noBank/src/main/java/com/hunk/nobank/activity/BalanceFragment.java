package com.hunk.nobank.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hunk.nobank.Core;
import com.hunk.nobank.R;
import com.hunk.nobank.contract.AccountType;
import com.hunk.nobank.manager.AccountDataManager;
import com.hunk.nobank.manager.dataBasic.ManagerListener;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.manager.dataBasic.ViewManagerListener;
import com.hunk.nobank.model.AccountSummaryPackage;

public class BalanceFragment extends Fragment {

    private TextView mBalance;
    private ImageView mUserLogo;
    private UserManager mUserManager;
    private AccountDataManager mMainAccountDataManager;
    private AccountDataManager mVaultAccountDataManager;
    private ViewManagerListener mViewManagerListener = new ViewManagerListener(this) {
        @Override
        public void onSuccess(String managerId, String messageId, Object data) {
            if (managerId.equals(mUserManager.getManagerId())) {
                if (messageId.equals(UserManager.METHOD_ACCOUNT_SUMMARY)) {
                    if (UserManager.isPostLogin(mUserManager)) {
                        mMainAccountDataManager = mUserManager.getCurrentUserSession().getAccountDataManagerByType(AccountType.Main);
                        mVaultAccountDataManager = mUserManager.getCurrentUserSession().getAccountDataManagerByType(AccountType.Vault);

                        mBalance.setText(mMainAccountDataManager.getAccountModel().Balance.string());
                    } else {
                        // TODO: throw exception in debug mode, or clean listener after session expire.
                    }
                }
            } else {

            }
        }

        @Override
        public void onFailed(String managerId, String messageId, Object data) {

        }
    };

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_balance, container, false);
        setupUI(view);
        return view;
    }

    private void setupUI(View view) {
        mBalance = (TextView) view.findViewById(R.id.txt_balance);
        mUserLogo = (ImageView) view.findViewById(R.id.user_logo);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mUserManager.fetchAccountSummary(new AccountSummaryPackage(), mViewManagerListener)) {
            mBalance.setText(R.string.loading_balance);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserManager = Core.getInstance().getUserManager();
        mUserManager.registerViewManagerListener(mViewManagerListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUserManager.unregisterViewManagerListener(mViewManagerListener);
    }
}
