package com.hunk.nobank.activity.dashboard;

import com.hunk.abcd.activity.mvp.BasePresenter;
import com.hunk.abcd.activity.mvp.BaseView;
import com.hunk.nobank.contract.Money;
import com.hunk.nobank.contract.TransactionFields;

import java.util.List;

/**
 * @author HunkDeng
 * @since 2016/5/23
 */
public interface DashboardView<P extends BasePresenter> extends BaseView<P> {
    void showBalance(Money balance);
    void showLoadingBalance();
    void showTransactionList(List<TransactionFields> mTransactionList);
    void showLoadingTransaction();
}
