package com.hunk.test.nobank.activity.root;

import com.hunk.abcd.activity.mvp.BasePresenter;
import com.hunk.nobank.activity.root.RootActivity;
import com.hunk.nobank.activity.root.RootView;
import com.hunk.test.utils.NBAbstractTest;

import org.junit.Test;

import static org.robolectric.Robolectric.setupActivity;

/**
 * @author HunkDeng
 * @since 2016/6/9
 */
public class RootViewTest extends NBAbstractTest implements RootView {

    @Test
    public void smoke() {
        setupActivity(RootActivity.class);
    }

    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
