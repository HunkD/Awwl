package com.hunk.nobank.manager.dataBasic;

import java.util.HashMap;
import java.util.Map;

public abstract class DataManager {
    // TODO: Remove this method
    public abstract String getManagerId();

    private final Map<String, ViewManagerListener> listeners = new HashMap<>();

    public void registerViewManagerListener(ViewManagerListener viewManagerListener) {
        synchronized (listeners) {
            listeners.put(viewManagerListener.getId(), viewManagerListener);
        }
    }

    public void unregisterViewManagerListener(ViewManagerListener viewManagerListener) {
        if (listeners.containsKey(viewManagerListener.getId())) {
            listeners.remove(viewManagerListener.getId());
        }
    }

    public ViewManagerListener getViewManagerListener(String id) {
        synchronized (listeners) {
            if (listeners.containsKey(id)) {
                return listeners.get(id);
            }
            return null;
        }
    }

    public void triggerSuccess(String id, String managerId, String messageId, Object data) {
        ViewManagerListener listener = getViewManagerListener(id);
        if (listener != null) {
            listener.success(managerId, messageId, data);
        }
    }

    public void triggerFailed(String id, String managerId, String messageId, Object data) {
        ViewManagerListener listener = getViewManagerListener(id);
        if (listener != null) {
            listener.failed(managerId, messageId, data);
        }
    }
}
