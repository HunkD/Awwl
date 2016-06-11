package com.hunk.nobank.activity.welcome;

import android.support.annotation.VisibleForTesting;

import com.hunk.nobank.Core;
import com.hunk.nobank.activity.base.AbstractPresenter;
import com.hunk.nobank.activity.base.BasePresenter;
import com.hunk.nobank.manager.UserManager;
import com.hunk.whitelabel.retailer.RetailerFeatureList;

/**
 * @author HunkDeng
 * @since 2016/5/21
 */
public class WelcomePagePresenter extends AbstractPresenter<WelcomeView> {
    @VisibleForTesting
    private boolean mCheckRememberMe = true;

    public void onCreate() {
        mView.showSignUp(RetailerFeatureList.Registration.ENABLE);
    }

    public void onResume() {
        // check remember me when this is first time came to welcome page.
        if (mCheckRememberMe) {
            mCheckRememberMe = false;

            UserManager userManager = Core.getInstance().getUserManager();
            if (userManager.isRememberMe()) {
                // go to login screen
                mView.onClickSignIn(null);
            }
        }
    }
}
