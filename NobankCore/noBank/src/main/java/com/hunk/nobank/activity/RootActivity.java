package com.hunk.nobank.activity;

import android.content.Intent;

import com.hunk.nobank.Core;
import com.hunk.nobank.NConstants;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.util.Logging;
import com.hunk.whitelabel.Feature;
import com.hunk.whitelabel.FeatureConfigs;

/**
 * The Root for Activity Stack of this application
 */
public class RootActivity extends BaseActivity {

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (isExit(intent)) {
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // ask Login feature if the user already login.
        UserManager userManager = Core.getInstance().getLoginManager();

        if (userManager.isLogInSuccessfully()) {
            if (FeatureConfigs.FEATURE_DASHBOARD) {
                // go to Welcome Screen
                Intent gotoWelcome = new Intent();
                gotoWelcome.setPackage(getApplicationContext().getPackageName());
                gotoWelcome.setAction(generateAction(Feature.dashboard, NConstants.OPEN_MAIN));
                if (gotoWelcome.resolveActivity(getPackageManager()) != null) {
                    startActivity(gotoWelcome);
                } else {
                    Logging.w("No welcome feature when calling in RootActivity.");
                }
            }
        }

        if (FeatureConfigs.FEATURE_WELCOME) {
            // go to Welcome Screen
            Intent gotoWelcome = new Intent();
            gotoWelcome.setPackage(getApplicationContext().getPackageName());
            gotoWelcome.setAction(generateAction(Feature.welcome, NConstants.OPEN_MAIN));
            if (gotoWelcome.resolveActivity(getPackageManager()) != null) {
                startActivity(gotoWelcome);
            } else {
                Logging.w("No welcome feature when calling in RootActivity.");
            }
        }
    }

    public boolean isExit(Intent newIntent) {
        return newIntent.getBooleanExtra("exit", false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        getHijackingNotification().dismiss();
    }
}
