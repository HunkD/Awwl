package com.hunk.nobank.manager.dataBasic;

import java.lang.ref.WeakReference;

/**
 * Since Android will recycle the activity or fragment sometimes, but our listener is holding in manager layer.
 * Which means when activity has been recycled and Callback return at same time,
 * if call any method like 'dismissDialog' in callback method, it will provoke NullPointerException.
 */
public abstract class ViewManagerListener implements ManagerListener {

    private final WeakReference<Object> weakRefer;

    public ViewManagerListener(Object o) {
        this.weakRefer = new WeakReference<>(o);
    }

    @Override
    public void success(String managerId, String messageId, Object data) {
        if (weakRefer.get() != null) {
            onSuccess(managerId, messageId, data);
        }
    }

    @Override
    public void failed(String managerId, String messageId, Object data) {
        if (weakRefer.get() != null) {
            onFailed(managerId, messageId, data);
        }
    }

    public abstract void onSuccess(String managerId, String messageId, Object data);

    public abstract void onFailed(String managerId, String messageId, Object data);
}
