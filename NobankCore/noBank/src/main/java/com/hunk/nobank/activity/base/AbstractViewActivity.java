package com.hunk.nobank.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

/**
 * @author HunkDeng
 * @since 2016/6/8
 */
public abstract class AbstractViewActivity<P extends BasePresenter>
        extends FragmentActivity implements BaseView<P> {

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.attach(this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detach();
        super.onDestroy();
    }

    @Override
    public void setPresenter(P presenter) {
        mPresenter = presenter;
    }

    @Override
    public P getPresenter() {
        return mPresenter;
    }
}
