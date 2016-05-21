package com.hunk.nobank.activity.welcome;

import android.view.View;

/**
 * @author HunkDeng
 * @since 2016/5/21
 */
public interface WelcomeView {
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
}
