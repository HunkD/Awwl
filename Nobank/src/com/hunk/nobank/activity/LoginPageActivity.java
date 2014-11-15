package com.hunk.nobank.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hunk.nobank.R;
import com.hunk.nobank.util.StringUtils;

public class LoginPageActivity extends Activity {
	
	private EditText mInputLoginName;
	private EditText mInputLoginPsd;
	private Button mBtnLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_login);
		
		setupUI();
	}

	private void setupUI() {
		// ---findViews---
		mInputLoginName = (EditText) findViewById(R.id.login_page_input_login_name);
		mInputLoginPsd = (EditText) findViewById(R.id.login_page_input_password);		
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
				// TODO Auto-generated method stub
				
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

}
