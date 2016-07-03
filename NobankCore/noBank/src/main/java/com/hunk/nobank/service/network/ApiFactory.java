package com.hunk.nobank.service.network;

/**
 * @author HunkDeng
 * @since 2016/7/3
 */
public interface ApiFactory {
    <T> T get(Class<T> userServiceClass);
}
