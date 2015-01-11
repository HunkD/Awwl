package com.hunk.nobank.util;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Message;

public abstract class WeakHandler<T> extends Handler {
	private WeakReference<T> mAct;	
	
	public WeakHandler(T mAct) {
		super();
		this.mAct = new WeakReference<T>(mAct);
	}
	
	@Override
	public final void handleMessage(Message msg) {
		if (mAct.get() != null) {
			handleMessageSafely(msg, mAct.get());
		}
	}

	public abstract void handleMessageSafely(Message msg, T act);

	public T getmAct() {
		return mAct.get();
	}
}
