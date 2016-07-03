package com.hunk.abcd.activity.mvp;

import android.support.annotation.VisibleForTesting;

/**
 * Abstract Presenter which hold common logic.
 * Such as attach() and detach()
 *
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
