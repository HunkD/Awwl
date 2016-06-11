package com.hunk.nobank.activity.base;

import android.support.annotation.VisibleForTesting;

/**
 * @author HunkDeng
 * @since 2016/6/8
 */
public abstract class AbstractPresenter<V extends BaseView> implements BasePresenter<V> {
    @VisibleForTesting
    protected V mView;

    @Override
    public void attach(V view) {
        mView = view;
    }

    @Override
    public void detach() {
        mView = null;
    }

    @Override
    public V getView() {
        return mView;
    }
}
