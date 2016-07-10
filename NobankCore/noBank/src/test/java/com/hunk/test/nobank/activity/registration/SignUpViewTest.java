package com.hunk.test.nobank.activity.registration;

import com.hunk.abcd.activity.mvp.BasePresenter;
import com.hunk.nobank.activity.registration.SignUpActivity;
import com.hunk.nobank.activity.registration.SignUpView;
import com.hunk.test.utils.NBAbstractTest;

import org.junit.Test;

import static org.robolectric.Robolectric.setupActivity;

/**
 * @author HunkDeng
 * @since 2016/6/9
 */
public class SignUpViewTest extends NBAbstractTest implements SignUpView {

    @Test
    public void smoke() {
        setupActivity(SignUpActivity.class);
    }

    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }

    /**
     * @see #showError()
     * @param e
     */
    @Deprecated
    @Override
    public void showError(Throwable e) {

    }

    @Test
    public void showError() {

    }
}
