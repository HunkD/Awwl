package com.hunk.nobank.activity.dashboard;

import com.hunk.abcd.activity.mvp.BasePresenter;
import com.hunk.abcd.activity.mvp.BaseView;

/**
 * @author HunkDeng
 * @since 2016/5/29
 */
public interface DashboardPresenter<V extends BaseView> extends BasePresenter<V> {

    void onResume();

    void forceRefreshAction();

    void showMoreTransactionsAction();
}
