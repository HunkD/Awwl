package com.hunk.nobank;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.hunk.nobank.core.CoreService;
import com.hunk.nobank.feature.Feature;
import com.hunk.nobank.util.Logging;

/**
 * The Root for Activity Stack of this application
 */
public class RootActivity extends BaseActivity {
    // result receiver for CoreService
    private ResultReceiver mReceiver = new ResultReceiver(new Handler()) {
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            String action = resultData.getString(CoreService.RESULT_ACTION);
            String resultGson = resultData.getString(CoreService.RESULT_DATA);

            if (generateAction(Feature.login, NConstants.GET_REMEMBER_ME).equals(action)) {
                if (resultGson != null && resultGson.equals(Boolean.TRUE.toString())) {
                    // go to login screen
                    Logging.d("go to login screen");
                } else {
                    // go to Welcome Screen
                    Logging.d("go to welcome screen");
                }
            }
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getBooleanExtra("exit", false)) {
            finish();
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // ask Login feature if the user already login.
        CoreService.startCoreService(this,
                generateAction(Feature.login, NConstants.GET_REMEMBER_ME), null, mReceiver);
    }


}
