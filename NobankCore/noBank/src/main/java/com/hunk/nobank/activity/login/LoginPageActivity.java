package com.hunk.nobank.activity.login;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.hunk.nobank.NoBankApplication;
import com.hunk.nobank.R;
import com.hunk.nobank.activity.BaseActivity;
import com.hunk.nobank.activity.root.RootActivity;

public class LoginPageActivity
        extends BaseActivity<LoginPagePresenter>
        implements LoginView<LoginPagePresenter> {

    public static final String ACTION = "action.login.open_main";

    private EditText mInputLoginName;
    @VisibleForTesting
    private EditText mInputLoginPsd;
    private CheckBox mRememberMe;

    {
        setPresenter(new LoginPagePresenter());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login, Base.NO_DRAW_LAYOUT);
        application = (NoBankApplication) getApplication();
        setupUI();
    }

    private void setupUI() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setElevation(ViewHelper.pxFromDp(this, R.dimen.title_bar_shadow_elevation));
        // ---findViews---
        mInputLoginName = (EditText) findViewById(R.id.login_page_input_login_name);
        mInputLoginPsd = (EditText) findViewById(R.id.login_page_input_password);
        mRememberMe = (CheckBox) findViewById(R.id.login_page_remember_me);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // set dynamic information
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public void navigateToDashboard() {
        RootActivity.unrollActivity(LoginPageActivity.this);
    }

    @Override
    public void clearPassword() {
        mInputLoginPsd.setText("");
    }

    @Override
    public void showRememberedUserName(String username) {
        mInputLoginName.setText(username);
        mRememberMe.setChecked(true);
    }

    @Override
    public boolean isCheckedRememberMe() {
        return mRememberMe.isChecked();
    }

    @Override
    public String getUserName() {
        return mInputLoginName.getText().toString();
    }

    @Override
    public String getPsd() {
        return mInputLoginPsd.getText().toString();
    }

    @Override
    public void navigateToSecurityQuestion() {

    }

    public void onClickLogin(View view) {
        mPresenter.loginAction();
    }
}
