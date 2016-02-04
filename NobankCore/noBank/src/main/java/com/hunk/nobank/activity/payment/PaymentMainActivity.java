package com.hunk.nobank.activity.payment;

import android.os.Bundle;

import com.hunk.nobank.R;
import com.hunk.nobank.activity.BaseActivity;

public class PaymentMainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_main, Base.NORMAL);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        unrollActivity(this);
    }
}
