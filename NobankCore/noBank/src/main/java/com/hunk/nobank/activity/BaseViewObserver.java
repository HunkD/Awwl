package com.hunk.nobank.activity;

import com.hunk.abcd.activity.mvp.BasePresenter;

import rx.Observer;

/**
 * @author HunkDeng
 * @since 2016/7/8
 */
public abstract class BaseViewObserver<T> implements Observer<T> {

    private BasePresenter mPresenter;

    public BaseViewObserver(BasePresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (mPresenter.getView() != null) {
            mPresenter.getView().showError(e);
        }
    }
}
