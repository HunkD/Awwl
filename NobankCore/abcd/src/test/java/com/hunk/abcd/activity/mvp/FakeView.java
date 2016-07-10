package com.hunk.abcd.activity.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hunk.abcd.R;

import org.junit.Test;

/**
 * @author HunkDeng
 * @since 2016/7/3
 */
public class FakeView
        extends AbstractViewActivity<IFakePresenter>
        implements IFakeView<IFakePresenter> {
    {
        setPresenter(new FakePresenter());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void showError(Throwable e) {

    }
}
