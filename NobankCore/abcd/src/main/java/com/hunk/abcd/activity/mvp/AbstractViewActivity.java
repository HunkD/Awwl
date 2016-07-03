package com.hunk.abcd.activity.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Abstract Activity to hold common logic for Base View. <br>
 * 1. attach and detach self to presenter.
 *
 * @author HunkDeng
 * @since 2016/6/8
 */
public abstract class AbstractViewActivity<P extends BasePresenter>
        extends AppCompatActivity implements BaseView<P> {

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO don't know how to resolve this warning...
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
