package com.hunk.nobank.feature.interfaces;


public abstract class Client {

	public interface Callback {
		public abstract void onSuccess(String result);
		public abstract void onFailed();
	}

	public abstract void post(String url, String json, Callback callback);
}
