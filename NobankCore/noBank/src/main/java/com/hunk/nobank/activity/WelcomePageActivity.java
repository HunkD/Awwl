package com.hunk.nobank.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hunk.nobank.Core;
import com.hunk.nobank.NConstants;
import com.hunk.nobank.R;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.views.SlideButtonLayout;
import com.hunk.whitelabel.Feature;

public class WelcomePageActivity extends AccountBaseActivity {
    private View btnSignIn;
    private View btnSignUp;
    private SlideButtonLayout slideBtn;

    private Intent gotoLogin;
    private Intent gotoRegistration;

    private boolean checkRememberMe = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

            UserManager userManager = Core.getInstance().getLoginManager();
            if (userManager.isRememberMe()) {
                // go to login screen
                Intent gotoLogin = new Intent();
                gotoLogin.setPackage(getApplicationContext().getPackageName());
                gotoLogin.setAction(generateAction(Feature.login, NConstants.OPEN_MAIN));
                startActivity(gotoLogin);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(generateAction(Feature.root, NConstants.OPEN_MAIN));
        intent.putExtra("exit", true);
        intent.setPackage(getApplicationContext().getPackageName());
        startActivity(intent);
    }

    public void forInstrumentTest() {
        Intent gotoLogin = new Intent();
        gotoLogin.setPackage(getApplicationContext().getPackageName());
        gotoLogin.setAction(generateAction(Feature.login, NConstants.OPEN_MAIN));
        startActivity(gotoLogin);
    }
}
