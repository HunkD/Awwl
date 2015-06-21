package com.hunk.nobank.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.hunk.nobank.Core;
import com.hunk.nobank.NoBankApplication;
import com.hunk.nobank.R;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.manager.ViewManagerListener;
import com.hunk.nobank.model.AccountSummaryPackage;
import com.hunk.nobank.model.LoginReqPackage;
import com.hunk.nobank.util.StringUtils;

public class LoginPageActivity extends AccountBaseActivity {

    private EditText mInputLoginName;
    private EditText mInputLoginPsd;
    private CheckBox mRememberMe;
    private Button mBtnLogin;

    private NoBankApplication application;
    private UserManager mUserManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login, Base.NO_DRAW_LAYOUT);
        application = (NoBankApplication) getApplication();
        mUserManager = Core.getInstance().getLoginManager();
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
        mBtnLogin = (Button) findViewById(R.id.login_page_login_btn);

        // ---setListeners---
        mBtnLogin.setTypeface(null, Typeface.NORMAL);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    submit();
                } else {
                    Toast.makeText(getApplicationContext(), "Fill all info, please.", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public void submit() {
        final LoginReqPackage req = getLoginReq();
        showLoading();
        mUserManager.fetchLogin(req, mManagerListener);
    }

    public boolean checkInput() {
        boolean pass = true;
        if (StringUtils.isNullOrEmpty(mInputLoginName.getText().toString())) {
            pass = false;
        }
        if (StringUtils.isNullOrEmpty(mInputLoginPsd.getText().toString())) {
            pass = false;
        }
        return pass;
    }

    public LoginReqPackage getLoginReq() {
        LoginReqPackage req = new LoginReqPackage(
                mInputLoginName.getText().toString(),
                mInputLoginPsd.getText().toString(),
                mRememberMe.isChecked());
        return req;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // set dynamic information

        if (mUserManager.isRememberMe()) {
            mRememberMe.setChecked(true);
            mInputLoginName.setText(mUserManager.getRememberMeUserName());
        } else {
            // should not reset this in onResume(), do this after restart this application.
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // save Remember Me Status
        if (mRememberMe.isChecked()) {
            mUserManager.setRememberMe(true, mInputLoginName.getText().toString());
        } else {
            mUserManager.setRememberMe(false, null);
        }
    }

    private ViewManagerListener mManagerListener = new ViewManagerListener(this) {
        @Override
        public void onSuccess(String managerId, String messageId, Object data) {
            if (managerId.equals(mUserManager.getManagerId())) {
                if (messageId.equals(UserManager.METHOD_LOGIN)) {
                    if (mUserManager.isLogInSuccessfully()) {
                        AccountSummaryPackage accountSummaryPackage = new AccountSummaryPackage();
                        mUserManager.fetchAccountSummary(accountSummaryPackage, this);
                    } else {
                        dismissLoading();
                        Toast.makeText(LoginPageActivity.this, "verifySecurityQuestion", Toast.LENGTH_SHORT).show();
                    }
                } else if (messageId.equals(UserManager.METHOD_ACCOUNT_SUMMARY)) {
                    dismissLoading();
                    gotoNextActivity(LoginPageActivity.this);
                }
            } else {

            }
        }

        @Override
        public void onFailed(String managerId, String messageId, Object data) {
            if (managerId.equals(mUserManager.getManagerId())) {
                if (messageId.equals(UserManager.METHOD_LOGIN)) {
                    dismissLoading();
                    Toast.makeText(LoginPageActivity.this, "failed", Toast.LENGTH_SHORT).show();
                }
            } else {

            }
        }
    };
}
