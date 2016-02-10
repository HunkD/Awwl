package com.hunk.nobank.activity;

import android.content.Intent;

import com.hunk.whitelabel.retailer.RetailerFeatureList;

public class AccountBaseActivity extends BaseActivity {
    public void gotoNextActivity(AccountBaseActivity act) {
        Intent next = new Intent();
        next.setPackage(getPackageName());
        next.setAction(RetailerFeatureList.Dashboard.ACTION);
        this.startActivity(next);
    }
}
