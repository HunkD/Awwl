package com.hunk.reference;

import com.hunk.nobank.NoBankApplication;
import com.hunk.nobank.feature.interfaces.Client;
import com.hunk.stubserver.StubClient;

public class ReferenceApplication extends NoBankApplication {

	Client mClient;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mClient = new StubClient();
	}

	@Override
	public Client getClient() {
		return mClient;
	}	

}
