package com.hunk.nobank;

import com.hunk.nobank.controller.RoutingController;

import android.app.Application;

public class NoBankApplication extends Application {
	RoutingController rc;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		rc = new RoutingController();		
		super.onCreate();
	}

	public RoutingController getRoutingController() {
		return rc;
	}

	public boolean isSignIn() {
		// TODO Auto-generated method stub
		return false;
	}
}
