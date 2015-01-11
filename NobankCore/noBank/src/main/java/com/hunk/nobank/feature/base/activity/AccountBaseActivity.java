package com.hunk.nobank.feature.base.activity;

import android.content.Intent;

import com.hunk.nobank.BaseActivity;

public class AccountBaseActivity extends BaseActivity {
	public void gotoNextActivity(AccountBaseActivity act) {
		Intent next = new Intent();
		next.setAction(getPackageName() + ".action.goto.base.dashboard");
		this.startActivity(next);
	}
}
