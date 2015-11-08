package com.hunk.nobank.activity.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hunk.nobank.Core;
import com.hunk.nobank.R;
import com.hunk.nobank.activity.BaseActivity;
import com.hunk.nobank.extension.view.PersonalInfoItemView;

/**
 *
 */
public class PersonalInfoActivity extends BaseActivity {
    private PersonalInfoItemView mEmailInfo;
    private PersonalInfoItemView mPhoneInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_page, Base.NORMAL);
        getTitleBarPoxy().getTitle().setText(R.string.personal_info);

        setupUI();
    }

    private void setupUI() {
        mEmailInfo = (PersonalInfoItemView) findViewById(R.id.email_info);
        mPhoneInfo = (PersonalInfoItemView) findViewById(R.id.phone_info);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        Button verifyBtn = mEmailInfo.getVerifyBtn();
        verifyBtn.setVisibility(View.VISIBLE);
        if (Core.getInstance().getLoginManager().isLogInSuccessfully()) {
            verifyBtn.setText("Verified");
        } else {
            verifyBtn.setText("Verify");
        }
    }
}
