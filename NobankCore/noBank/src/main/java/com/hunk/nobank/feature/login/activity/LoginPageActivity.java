package com.hunk.nobank.feature.login.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.hunk.nobank.NoBankApplication;
import com.hunk.nobank.R;
import com.hunk.nobank.core.CoreService;
import com.hunk.nobank.feature.Feature;
import com.hunk.nobank.feature.base.activity.AccountBaseActivity;
import com.hunk.nobank.feature.base.manager.AccountManager;
import com.hunk.nobank.feature.base.model.LoginReq;
import com.hunk.nobank.feature.interfaces.SequenceRequest;
import com.hunk.nobank.feature.login.manager.LoginManager;
import com.hunk.nobank.util.StringUtils;
import com.hunk.nobank.util.WeakHandler;

public class LoginPageActivity extends AccountBaseActivity {
	
	private EditText mInputLoginName;
	private EditText mInputLoginPsd;
	private CheckBox mRememberMe;
	private Button mBtnLogin;
	
	private MyHandler mHandler;
	private NoBankApplication application;
    private LoginManager mLoginManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_login);
		application = (NoBankApplication)getApplication();
        mLoginManager =
                (LoginManager) CoreService.mRegisteredFeatureManager.get(Feature.login.toString());
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
						SequenceRequest sq = new SequenceRequest();
						sq.addRequestHandler(AccountManager.loginPoster.generate(req));
						boolean isSuccess = sq.execute();
						Message message = null;
						if (isSuccess) {
							message = mHandler.obtainMessage(MyHandler.LOGIN_SUCCESS);
						} else {
							message = mHandler.obtainMessage(MyHandler.LOGIN_FAILED);
						}
						mHandler.sendMessage(message);
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

		if (mLoginManager.isRememberMe()) {
			mRememberMe.setChecked(true);
			mInputLoginName.setText(mLoginManager.getRememberMeUserName());
		} else {
			// should not reset this in onResume(), do this after restart this application.
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// save Remember Me Status
		if (mRememberMe.isChecked()) {
			mLoginManager.setRememberMe(true, mInputLoginName.getText().toString());
		} else {
			mLoginManager.setRememberMe(false, null);
		}
	}
	
	private static class MyHandler extends WeakHandler<LoginPageActivity> {

		private final static int LOGIN_SUCCESS = 1;
		private final static int LOGIN_FAILED = 2;
		
		public MyHandler(LoginPageActivity act) {
			super(act);
		}

		@Override
		public void handleMessageSafely(Message msg, LoginPageActivity activity) {
			switch(msg.what) {
			case LOGIN_SUCCESS:
				activity.gotoNextActivity(activity);
				break;
			case LOGIN_FAILED:
				Toast.makeText(activity, "failed", Toast.LENGTH_SHORT).show();
				break;
			}
		}		
	}
}
