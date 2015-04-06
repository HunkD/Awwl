package com.hunk.nobank.feature.welcome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;

import com.hunk.nobank.BaseActivity;
import com.hunk.nobank.NConstants;
import com.hunk.nobank.R;
import com.hunk.nobank.core.CoreService;
import com.hunk.nobank.feature.Feature;
import com.hunk.nobank.feature.base.activity.AccountBaseActivity;
import com.hunk.nobank.views.SlideButtonLayout;

public class WelcomePageActivity extends AccountBaseActivity {
	private View btnSignIn;
	private View btnSignUp;
    private SlideButtonLayout slideBtn;

    private Intent gotoLogin;
    private Intent gotoRegistration;

    private boolean checkRememberMe = true;
    // result receiver for CoreService
    private ResultReceiver mReceiver = new ResultReceiver(new Handler()) {
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            String action = resultData.getString(CoreService.RESULT_ACTION);
            String resultGson = resultData.getString(CoreService.RESULT_DATA);

            if (generateAction(Feature.login, NConstants.GET_REMEMBER_ME).equals(action)) {
                if (resultGson != null && resultGson.equals(Boolean.TRUE.toString())) {
                    // go to login screen
                    Intent gotoLogin = new Intent();
                    gotoLogin.setPackage(getApplicationContext().getPackageName());
                    gotoLogin.setAction(generateAction(Feature.login, NConstants.OPEN_MAIN));
                    startActivity(gotoLogin);
                }
            }
        }
    };

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_page);
        setBaseStyle(Base.NO_DRAW_LAYOUT);
        setBaseStyle(Base.NO_TITLE_BAR);

		setupUI();
	}

	private void setupUI() {
		// ---intent prepare---
        gotoLogin = new Intent();
        gotoLogin.setPackage(getApplicationContext().getPackageName());
        gotoLogin.setAction(generateAction(Feature.login, NConstants.OPEN_MAIN));

        gotoRegistration = new Intent();
        gotoRegistration.setPackage(getApplicationContext().getPackageName());
        gotoRegistration.setAction(generateAction(Feature.registration, NConstants.OPEN_MAIN));
		// ---findViews---
		btnSignUp = findViewById(R.id.welcome_btn_sign_up);
		btnSignIn = findViewById(R.id.welcome_btn_sign_in);

		// ---setListener---
        if (gotoRegistration.resolveActivity(getPackageManager()) != null) {
            btnSignUp.setVisibility(View.VISIBLE);
            btnSignUp.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    startActivity(gotoRegistration);
                }
            });
        }
        if (gotoLogin.resolveActivity(getPackageManager()) != null) {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignIn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    startActivity(gotoLogin);
                }
            });
        }
	}

    @Override
	protected void onResume() {
		super.onResume();
        if (checkRememberMe) {
            checkRememberMe = false;
            // ask Login feature if the user already login.
            CoreService.startCoreService(this,
                    generateAction(Feature.login, NConstants.GET_REMEMBER_ME), null, mReceiver);
        }
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getPackageName() + BaseActivity.ACTION_GOTO_ROOT);
		intent.putExtra("exit", true);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setPackage(getApplicationContext().getPackageName());
		startActivity(intent);
	}
}
