package com.hunk.nobank.feature.base.activity;

import android.content.Intent;

import com.hunk.nobank.BaseActivity;
import com.hunk.nobank.NConstants;
import com.hunk.nobank.feature.Feature;

public class AccountBaseActivity extends BaseActivity {
	public void gotoNextActivity(AccountBaseActivity act) {
		Intent next = new Intent();
		next.setPackage(getPackageName());
		next.setAction(BaseActivity.generateAction(Feature.dashboard, NConstants.OPEN_MAIN));
		this.startActivity(next);
	}
}
