package com.hunk.nobank.activity.dashboard;

/**
 * @author HunkDeng
 * @since 2016/5/29
 */
public interface DashboardPresenter {

    void onResume();

    void onDestroy();

    void forceRefreshAction();

    void firstTimeResume();
}
