package com.hunk.nobank;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class BaseActivity extends Activity {
	
	public final static String ACTION_GOTO_ROOT = ".action.goto.root";
	
	protected NoBankApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		application = (NoBankApplication)getApplication();
	}

	public void unrollActivity() {
		String packageName = this.getApplication().getPackageName();
		
		Intent unroll = new Intent();
		unroll.setAction(packageName + ACTION_GOTO_ROOT);
		unroll.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(unroll);
	}
}
