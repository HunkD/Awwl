package com.hunk.nobank.activity.dashboard;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.hunk.nobank.R;
import com.hunk.nobank.activity.BaseActivity;
import com.hunk.nobank.contract.Money;

public class DashboardActivity extends BaseActivity implements DashboardView {
    private DashboardPresenter mPresenter;

    private TextView mBalance;

    @Override
    protected boolean isRequiredLoginedUserSession() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_page, Base.NORMAL);
        // set Title
        getTitleBarPoxy().getTitle().setText(R.string.dashboard);
        mBalance = (TextView) findViewById(R.id.txt_balance);
        //
        mPresenter = new DashboardPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        BaseActivity.exitApplication(this);
    }

    @Override
    public void showBalance(Money balance) {
        mBalance.setText(balance.string());
    }

    @Override
    public void showLoadingBalance() {
        mBalance.setText(R.string.loading_balance);
    }

    @Override
    public ListView getListView() {
        return null;
    }
}
