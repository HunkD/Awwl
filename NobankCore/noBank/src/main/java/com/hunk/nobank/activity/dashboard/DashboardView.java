package com.hunk.nobank.activity.dashboard;

import com.hunk.nobank.contract.Money;

/**
 * @author HunkDeng
 * @since 2016/5/23
 */
public interface DashboardView {
    void showBalance(Money balance);
    void showLoadingBalance();
}
