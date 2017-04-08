package com.hunk.nobank.activity.login;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.EditText;

import com.hunk.abcd.extension.util.ViewHelper;
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

    private ColorDrawable mBackground;

    {
        setPresenter(new LoginPagePresenter());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login, Base.NO_TITLE_BAR_NO_DRAW_LAYOUT);
        application = (NoBankApplication) getApplication();
        setupUI();
        if (savedInstanceState == null &&
                ViewHelper.shouldShowActivityTransition(this)) {
            baseRootContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean onPreDraw() {
                    baseRootContainer.getViewTreeObserver().removeOnPreDrawListener(this);

                    long duration = 500;
                    ObjectAnimator bgAnim = ObjectAnimator.ofInt(mBackground, "alpha", 0, 255);
                    bgAnim.setDuration(duration);
                    bgAnim.start();

                    Animator animator = ViewAnimationUtils.createCircularReveal(
                            baseRootContainer,
                            baseRootContainer.getRight(),
                            baseRootContainer.getBottom(),
                            0,
                            baseRootContainer.getBottom());
                    animator.setDuration(duration);
                    animator.setInterpolator(new LinearInterpolator());
                    animator.start();
                    return true;
                }
            });
        } else {
            mBackground.setAlpha(255);
        }
    }

    private void setupUI() {
        // ---findViews---
        mInputLoginName = (EditText) findViewById(R.id.login_page_input_login_name);
        mInputLoginPsd = (EditText) findViewById(R.id.login_page_input_password);
        mRememberMe = (CheckBox) findViewById(R.id.login_page_remember_me);

        mBackground = (ColorDrawable) baseRootContainer.getBackground();
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

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        if (ViewHelper.shouldShowActivityTransition(this)) {
            long duration = 500;
            ObjectAnimator bgAnim = ObjectAnimator.ofInt(mBackground, "alpha", 255, 0);
            bgAnim.setDuration(duration);
            bgAnim.start();

            Animator animator = ViewAnimationUtils.createCircularReveal(
                    baseRootContainer,
                    baseRootContainer.getRight(),
                    baseRootContainer.getBottom(),
                    baseRootContainer.getBottom(),
                    0);
            animator.setDuration(duration);
            animator.setInterpolator(new LinearInterpolator());
            animator.start();

            baseRootContainer.postDelayed(this::finish, duration);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (ViewHelper.shouldShowActivityTransition(this)) {
            overridePendingTransition(0, 0);
        }
    }
}
