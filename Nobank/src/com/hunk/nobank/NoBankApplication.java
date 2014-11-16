package com.hunk.nobank;

import com.hunk.nobank.appconfig.ApplicationPreference;
import com.hunk.nobank.controller.RoutingController;

import android.app.Application;

public class NoBankApplication extends Application {
	RoutingController rc;
	ApplicationPreference mAppPreference;
	@Override
	public void onCreate() {			
		super.onCreate();
		rc = new RoutingController();	
		mAppPreference = new ApplicationPreference(this);
	}

	public RoutingController getRoutingController() {
		return rc;
	}

	public boolean isSignIn() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public ApplicationPreference getAppPreference() {
		return mAppPreference;
	}
	
}
