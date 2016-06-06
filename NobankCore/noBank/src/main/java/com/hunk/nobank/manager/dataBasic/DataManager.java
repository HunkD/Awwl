package com.hunk.nobank.manager.dataBasic;

import com.hunk.nobank.Core;
import com.hunk.nobank.extension.network.BaseReqPackage;
import com.hunk.nobank.model.Cache;

import java.util.HashMap;
import java.util.Map;

public abstract class DataManager {

    private final Map<String, ViewManagerListener> listeners = new HashMap<>();

    public void registerViewManagerListener(ViewManagerListener viewManagerListener) {
        synchronized (listeners) {
            listeners.put(viewManagerListener.getId(), viewManagerListener);
        }
    }

    public void unregisterViewManagerListener(ViewManagerListener viewManagerListener) {
        synchronized (listeners) {
            if (listeners.containsKey(viewManagerListener.getId())) {
                listeners.remove(viewManagerListener.getId());
            }
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

    public void cleanViewManagerListener() {
        synchronized (listeners) {
            listeners.clear();
        }
    }
    /**
     *
     * @return
     *  fetch from network or not
     */
    public boolean invokeNetwork(final String id,
                                 final Cache<?> cache, final BaseReqPackage baseReqPackage,
                                 final String managerId, final String methodId) {
        return invokeNetwork(id, cache, baseReqPackage, managerId, methodId, null);
    }

    /**
     *
     * @return
     *  fetch from network or not
     */
    public boolean invokeNetwork(final String id,
                                 final Cache<?> cache, final BaseReqPackage baseReqPackage,
                                 final String managerId, final String methodId,
                                 final SuccessCallBack successCallBack) {
        if (cache.shouldFetch(baseReqPackage)) {
            Core.getInstance().getNetworkHandler()
                    .fireRequest(new ManagerListener() {

                        @Override
                        public void success(String managerId, String messageId, Object data) {
                            if (successCallBack != null) {
                                if (successCallBack.success(managerId, messageId, data)) {
                                    triggerSuccess(id, managerId, messageId, data);
                                } else {
                                    triggerFailed(id, managerId, messageId, data);
                                }
                            } else {
                                triggerSuccess(id, managerId, messageId, data);
                            }
                        }

                        @Override
                        public void failed(String managerId, String messageId, Object data) {
                            triggerFailed(id, managerId, messageId, data);
                        }
                    }, baseReqPackage, managerId, methodId);
            return true;
        } else {
            triggerSuccess(id, managerId, methodId, cache.get());
            return false;
        }
    }

    public interface SuccessCallBack {
        boolean success(String managerId, String messageId, Object data);
    }
}
