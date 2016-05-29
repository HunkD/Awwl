package com.hunk.nobank.activity.dashboard;

import com.hunk.nobank.contract.Money;
import com.hunk.nobank.contract.TransactionFields;

import java.util.List;

/**
 * @author HunkDeng
 * @since 2016/5/23
 */
public interface DashboardView {
    void showBalance(Money balance);
    void showLoadingBalance();
    void showTransactionList(List<TransactionFields> mTransactionList);
    void showLoadingTransaction();
}
