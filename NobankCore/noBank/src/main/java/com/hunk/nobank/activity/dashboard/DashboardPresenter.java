package com.hunk.nobank.activity.dashboard;

import com.hunk.nobank.activity.base.BasePresenter;
import com.hunk.nobank.activity.base.BaseView;

/**
 * @author HunkDeng
 * @since 2016/5/29
 */
public interface DashboardPresenter<V extends BaseView> extends BasePresenter<V> {

    void onResume();

    void forceRefreshAction();

    void firstTimeResume();

    void showMoreTransactionsAction();
}
