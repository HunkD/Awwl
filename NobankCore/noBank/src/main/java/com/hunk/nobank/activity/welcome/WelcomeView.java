package com.hunk.nobank.activity.welcome;

import android.view.View;

import com.hunk.abcd.activity.mvp.BasePresenter;
import com.hunk.abcd.activity.mvp.BaseView;

/**
 * @author HunkDeng
 * @since 2016/5/21
 */
public interface WelcomeView<P extends BasePresenter> extends BaseView<P> {
    /**
     * sign in action
     * @param view
     */
    void onClickSignIn(View view);

    /**
     * sign up action
     * @param view
     */
    void onClickSignUp(View view);

    /**
     *
     * @param show
     */
    void showSignUp(boolean show);
}
