package com.hunk.nobank.activity.base;

/**
 * @author HunkDeng
 * @since 2016/6/8
 */
public interface BasePresenter<V extends BaseView> {
    void attach(V view);
    void detach();

    V getView();
}
