package com.hunk.nobank.activity.dashboard.transaction;

/**
 *
 */
@Deprecated
public enum LoadingState {
    MORE_REFRESH,
    PULL_TO_REFRESH,
    UNKNOWN,
    INIT_REFRESH // First time user came to transaction list view.
}
