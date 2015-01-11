package com.hunk.nobank.feature.interfaces;

import java.lang.ref.WeakReference;

public abstract class FetchListener<Result> {
	
	private WeakReference<Object> mWeakReference;

	public FetchListener(Object o) {
		mWeakReference = new WeakReference<Object>(o); 
	}
	
	public void onReceiveSuccess(Result result) {
		if (mWeakReference.get() != null) {
			onSuccess(result);
		}
	}

	public abstract void onSuccess(Result result);
	
	public void onReceiveFailed() {
		if (mWeakReference.get() != null) {
			onFailed();
		}
	}
	
	public abstract void onFailed();
}
