package com.hunk.nobank.feature.base.activity;

import java.lang.ref.WeakReference;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.hunk.nobank.BaseActivity;
import com.hunk.nobank.NoBankApplication;
import com.hunk.nobank.R;
import com.hunk.nobank.feature.base.manager.AccountManager;
import com.hunk.nobank.feature.base.model.LoginReq;
import com.hunk.nobank.feature.base.model.LoginResp;
import com.hunk.nobank.feature.interfaces.FetchListener;
import com.hunk.nobank.util.StringUtils;

public class LoginPageActivity extends AccountBaseActivity {
	
	private EditText mInputLoginName;
	private EditText mInputLoginPsd;
	private CheckBox mRememberMe;
	private Button mBtnLogin;
	
	private MyHandler mHandler;
	private NoBankApplication application;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_login);
		application = (NoBankApplication)getApplication();
		mHandler = new MyHandler(this);
		setupUI();
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
				final LoginReq req = new LoginReq();
				req.username = mInputLoginName.getText().toString();
				req.password = mInputLoginPsd.getText().toString();
				
				new Thread(new Runnable() {

					@Override
					public void run() {
						AccountManager.loginPoster.fetch(req, new FetchListener<LoginResp>(this) {

							@Override
							public void onSuccess(LoginResp result) {
								Message msg = mHandler.obtainMessage(MyHandler.LOGIN_SUCCESS, result);
								mHandler.sendMessage(msg);
							}

							@Override
							public void onFailed() {
								Message msg = mHandler.obtainMessage(MyHandler.LOGIN_FAILED, null);
								mHandler.sendMessage(msg);
							}
							
						});
					}
					
				}).start();				
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
	
	private static class MyHandler extends Handler {

		private final static int LOGIN_SUCCESS = 1;
		private final static int LOGIN_FAILED = 2;
		
		private WeakReference<LoginPageActivity> mAct;
		
		public MyHandler(LoginPageActivity act) {
			mAct = new WeakReference<LoginPageActivity>(act);
		}
		
		@Override
		public void handleMessage(Message msg) {
			LoginPageActivity activity = mAct.get();
			if (activity != null) {
				switch(msg.what) {
				case LOGIN_SUCCESS:
					activity.gotoNextActivity(activity);
					break;
				case LOGIN_FAILED:
					Toast.makeText(activity, "failed", Toast.LENGTH_SHORT).show();
					break;
				}
				super.handleMessage(msg);
			}
		}		
	}
}
