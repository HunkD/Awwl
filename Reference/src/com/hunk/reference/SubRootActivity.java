package com.hunk.reference;

import android.content.Intent;

import com.hunk.nobank.RootActivity;

/**
 * Description :<br>
 * 
 */
public class SubRootActivity extends RootActivity {
	
	@Override
	protected void onNewIntent(Intent intent) {
		if (intent.getBooleanExtra("exit", false)) {
			finish();
		}  
		super.onNewIntent(intent);
	}

	@Override
	protected void onStart() {
		super.onResume();
		ReferenceApplication application = (ReferenceApplication)getApplication();
		if (application.isSignIn()) {
			
		} else {
			Intent intent = new Intent();
			intent.setAction(getPackageName() + ".action.goto.base.main");
			this.startActivity(intent);
		}
	}
	
}
