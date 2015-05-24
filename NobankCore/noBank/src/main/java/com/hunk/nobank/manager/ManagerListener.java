package com.hunk.nobank.manager;

public interface ManagerListener {
    void success(String managerId, String messageId, Object data);
    void failed(String managerId, String messageId, Object data);
}
