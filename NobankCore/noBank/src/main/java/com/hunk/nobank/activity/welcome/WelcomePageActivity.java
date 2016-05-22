package com.hunk.nobank.activity.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hunk.nobank.Core;
import com.hunk.nobank.R;
import com.hunk.nobank.activity.BaseActivity;
import com.hunk.nobank.activity.LoginPageActivity;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.util.ViewHelper;
import com.hunk.nobank.views.SlideButtonLayout;
import com.hunk.whitelabel.retailer.RetailerFeatureList;

public class WelcomePageActivity extends BaseActivity implements WelcomeView {
    private WelcomePagePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        setBaseStyle(Base.NO_DRAW_LAYOUT);
        setBaseStyle(Base.NO_TITLE_BAR);

        mPresenter = new WelcomePagePresenter(this);
        mPresenter.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        exitApplication(this);
    }

    public void onClickSignUp(View view) {
        Intent gotoRegistration = new Intent();
        gotoRegistration.setPackage(getApplicationContext().getPackageName());
        gotoRegistration.setAction(RetailerFeatureList.Registration.CardInfo.ACTION);

        startActivity(gotoRegistration);
    }

    @Override
    public void showSignUp(boolean show) {
        ViewHelper.showView(findViewById(R.id.welcome_btn_sign_up), show);
    }

    public void onClickSignIn(View view) {
        Intent gotoLogin = new Intent();
        gotoLogin.setPackage(getApplicationContext().getPackageName());
        gotoLogin.setAction(LoginPageActivity.ACTION);

        startActivity(gotoLogin);
    }
}
