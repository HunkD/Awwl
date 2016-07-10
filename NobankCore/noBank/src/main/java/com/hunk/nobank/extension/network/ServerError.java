package com.hunk.nobank.extension.network;

/**
 * @author HunkDeng
 * @since 2016/7/9
 */
public class ServerError extends Throwable {
    public final int Code;

    public ServerError(int code) {
        Code = code;
    }
}