package com.hunk.nobank.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hunk.nobank.NoBankApplication;
import com.hunk.nobank.R;
import com.hunk.nobank.controller.RoutingController;

public class WelcomePageActivity extends BaseActivity {
	RoutingController controller;
	private View btnSignIn;
	private View btnSignUp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_page);
		
		NoBankApplication application = (NoBankApplication)getApplication();
		controller = application.getRoutingController();
		
		setupUI();
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
				startActivity(new Intent(getPackageName()+ ".action.goto.signup"));
			}
		});
		btnSignIn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getPackageName()+ ".action.goto.signin"));
			}
		});
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getPackageName() + ".action.goto.root");
		intent.putExtra("exit", true);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
}
