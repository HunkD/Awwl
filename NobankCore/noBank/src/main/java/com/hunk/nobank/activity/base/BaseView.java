package com.hunk.nobank.activity.base;

/**
 * @author HunkDeng
 * @since 2016/6/8
 */
public interface BaseView<P extends BasePresenter> {
    void setPresenter(P presenter);
    P getPresenter();
}
