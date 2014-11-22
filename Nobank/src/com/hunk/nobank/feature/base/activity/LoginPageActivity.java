package com.hunk.nobank.feature.base.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.hunk.nobank.BaseActivity;
import com.hunk.nobank.Feature;
import com.hunk.nobank.NoBankApplication;
import com.hunk.nobank.R;
import com.hunk.nobank.feature.base.manager.AccountManager;
import com.hunk.nobank.util.StringUtils;

public class LoginPageActivity extends BaseActivity {
	
	private EditText mInputLoginName;
	private EditText mInputLoginPsd;
	private CheckBox mRememberMe;
	private Button mBtnLogin;
	private NoBankApplication application;

	private Object mAccountManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_login);
		application = (NoBankApplication)getApplication();
		findFeature();
		setupUI();
	}

	private void findFeature() {
		mAccountManager = application.getFeatureManager(Feature.ACCOUNT);
	}

	private void setupUI() {
		// ---findViews---
		mInputLoginName = (EditText) findViewById(R.id.login_page_input_login_name);
		mInputLoginPsd = (EditText) findViewById(R.id.login_page_input_password);		
		mRememberMe = (CheckBox) findViewById(R.id.login_page_remember_me);
		mBtnLogin = (Button) findViewById(R.id.login_page_login_btn);
		
		// ---setListeners---
		mBtnLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (checkInput()) {
					submit();			
				} else {
					Toast.makeText(getApplicationContext(), "Fill all info, please.", Toast.LENGTH_SHORT).show();
				}
			}

			private void submit() {
//				mAccountManager.loginPoster.fetch(new FetchListener());
			}

			private boolean checkInput() {
				boolean pass = true;
				if (StringUtils.isNullOrEmpty(mInputLoginName.getText().toString())) {
					pass = false;
				}
				if (StringUtils.isNullOrEmpty(mInputLoginPsd.getText().toString())) {
					pass = false;
				}
				return pass;
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// set dynamic information
		BaseFeaturePreference pref = application.getAppPreference();
		if (pref.isRememberMe()) {
			mRememberMe.setChecked(true);
			mInputLoginName.setText(pref.getRememberMeUserName());			
		} else {
			// should not reset this in onResume(), do this after restart this application.
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// save Remember Me Status
		BaseFeaturePreference pref = application.getAppPreference();
		if (mRememberMe.isChecked()) {
			pref.setRememberMe(true, mInputLoginName.getText().toString());
		} else {
			pref.setRememberMe(false, null);
		}
	}
}
