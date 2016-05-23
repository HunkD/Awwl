package com.hunk.nobank.activity.dashboard;

import android.widget.ListView;

import com.hunk.nobank.contract.Money;

/**
 * @author HunkDeng
 * @since 2016/5/23
 */
public interface DashboardView {
    void showBalance(Money balance);
    void showLoadingBalance();
    ListView getListView();
}
