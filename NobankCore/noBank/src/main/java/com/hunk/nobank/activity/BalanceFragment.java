package com.hunk.nobank.activity;

import android.app.Activity;
import android.os.Bundle;
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
import com.hunk.nobank.manager.ManagerListener;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.manager.ViewManagerListener;
import com.hunk.nobank.model.AccountSummaryPackage;

public class BalanceFragment extends Fragment {

    private TextView mBalance;
    private ImageView mUserLogo;
    private UserManager mUserManager;
    private AccountDataManager mMainAccountDataManager;
    private AccountDataManager mVaultAccountDataManager;
    private ManagerListener mViewManagerListener = new ViewManagerListener(this) {
        @Override
        public void onSuccess(String managerId, String messageId, Object data) {
            if (managerId.equals(mUserManager.getManagerId())) {
                if (messageId.equals(UserManager.METHOD_ACCOUNT_SUMMARY)) {
                    mMainAccountDataManager = mUserManager.getAccountDataManagerByType(AccountType.Main);
                    mVaultAccountDataManager = mUserManager.getAccountDataManagerByType(AccountType.Vault);

                    mBalance.setText(mMainAccountDataManager.getAccountModel().Balance.string());
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

        mBalance.setText(R.string.loading_balance);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mUserManager = Core.getInstance().getLoginManager();
    }

    @Override
    public void onResume() {
        super.onResume();
        mUserManager.fetchAccountSummary(new AccountSummaryPackage(), mViewManagerListener);
    }
}
