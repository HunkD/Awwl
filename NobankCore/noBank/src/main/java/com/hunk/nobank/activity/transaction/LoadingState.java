package com.hunk.nobank.activity.transaction;

/**
 *
 */
public enum LoadingState {
    MORE_REFRESH,
    PULL_TO_REFRESH,
    UNKNOWN,
    INIT_REFRESH // First time user came to transaction list view.
}
