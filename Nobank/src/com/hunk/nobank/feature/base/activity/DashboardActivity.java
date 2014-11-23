package com.hunk.nobank.feature.base.activity;

import android.os.Bundle;

import com.hunk.nobank.R;

public class DashboardActivity extends AccountBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard_page);
	}

}
