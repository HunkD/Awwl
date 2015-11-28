package com.hunk.nobank.activity.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hunk.nobank.R;
import com.hunk.nobank.activity.BaseActivity;
import com.hunk.nobank.service.registration.RegistrationForegroundService;

/**
 *
 */
public class SignUpActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        setBaseStyle(Base.NO_DRAW_LAYOUT);
        getTitleBarPoxy().getLeftIcon().setVisibility(View.GONE);

        startRegForegroundService();
        setupUI();
    }

    private void startRegForegroundService() {
        Intent intent = new Intent(this, RegistrationForegroundService.class);
        intent.setAction(RegistrationForegroundService.ACTION_START);
        startService(intent);
    }

    private void setupUI() {
        findViewById(R.id.btn_start_banking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, RegistrationForegroundService.class);
                intent.setAction(RegistrationForegroundService.ACTION_STOP);
                stopService(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
