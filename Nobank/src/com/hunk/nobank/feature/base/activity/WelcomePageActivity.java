package com.hunk.nobank.feature.base.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hunk.nobank.BaseActivity;
import com.hunk.nobank.NoBankApplication;
import com.hunk.nobank.R;

public class WelcomePageActivity extends AccountBaseActivity {
	private View btnSignIn;
	private View btnSignUp;
	
	private final String ACTION_NEXT_LOGIN = ".action.goto.base.login";
	private final String ACTION_NEXT_SIGNUP = ".action.goto.base.sigiup";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_page);
		
		NoBankApplication application = (NoBankApplication)getApplication();
		
		setupUI();
		
		if (application.getAppPreference().isRememberMe()){
			Intent intent = new Intent();
			intent.setAction(getPackageName() + ACTION_NEXT_LOGIN);
			this.startActivity(intent);
		}
	}

	private void setupUI() {
		// TODO Auto-generated method stub
		// ---findViews---
		btnSignUp = findViewById(R.id.welcome_btn_sign_up);
		btnSignIn = findViewById(R.id.welcome_btn_sign_in);
		// ---setListener---
		btnSignUp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getPackageName()+ ACTION_NEXT_SIGNUP));
			}
		});
		btnSignIn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getPackageName()+ ACTION_NEXT_LOGIN));
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();		
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getPackageName() + BaseActivity.ACTION_GOTO_ROOT);
		intent.putExtra("exit", true);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
}
