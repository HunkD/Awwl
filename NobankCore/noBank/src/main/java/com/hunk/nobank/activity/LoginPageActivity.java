package com.hunk.nobank.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

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
    private TextView mInputTextView;
    private ScrollView mSlContainer;
    private View mFakeKeypad;

    private NoBankApplication application;
    private UserManager mUserManager;
    private int mKeypadHeight;
    private int mSlContainerHeight;

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
        mInputTextView = (TextView) findViewById(R.id.login_textView);
        mFakeKeypad = findViewById(R.id.fake_keypad);
        mSlContainer = (ScrollView) findViewById(R.id.sl_container);

        mSlContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mSlContainerHeight = findViewById(R.id.sl_container).getHeight();

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    mSlContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    mSlContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

            }
        });

        mInputTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInputTextView.isFocused()) {
                    displayKeypad();
                } else {
                    mInputTextView.requestFocus();
                }
            }
        });
        mInputTextView.setFocusable(true);
        mInputTextView.setFocusableInTouchMode(true);
        mInputTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    displayKeypad();
                } else {
                    dismissKeypad();
                }
            }
        });

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

    private void displayKeypad() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mInputTextView.getWindowToken(), 0);

        if (mFakeKeypad.getVisibility() != View.VISIBLE) {
            mFakeKeypad.setVisibility(View.VISIBLE);

            if (mKeypadHeight == 0) {
                mFakeKeypad.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mKeypadHeight = findViewById(R.id.fake_keypad).getHeight();

                        extrudeScrollView();
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            mFakeKeypad.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            mFakeKeypad.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }

                    }
                });
            } else {
                extrudeScrollView();
            }
        }
    }

    private void dismissKeypad() {
        if (mFakeKeypad.getVisibility() == View.VISIBLE) {
            mFakeKeypad.setVisibility(View.GONE);

            mInputTextView.post(new Runnable() {
                @Override
                public void run() {
                    LayoutParams layoutParam =
                            (LayoutParams) mSlContainer.getLayoutParams();
                    LayoutParams newParam =
                            new LayoutParams(layoutParam.width, LayoutParams.MATCH_PARENT);
                    mSlContainer.setLayoutParams(newParam);
                    mSlContainer.setFillViewport(true);
                }
            });
        }
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

    private void extrudeScrollView() {
        mInputTextView.post(new Runnable() {
            @Override
            public void run() {
                LayoutParams layoutParam =
                        (LayoutParams) mSlContainer.getLayoutParams();
                LayoutParams newParam =
                        new LayoutParams(layoutParam.width, mSlContainerHeight - mKeypadHeight);
                mSlContainer.setLayoutParams(newParam);
                mSlContainer.setFillViewport(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mFakeKeypad.getVisibility() == View.VISIBLE) {
            dismissKeypad();
        } else {
            super.onBackPressed();
        }
    }
}
