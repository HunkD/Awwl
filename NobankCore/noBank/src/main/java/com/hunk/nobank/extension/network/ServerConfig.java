package com.hunk.nobank.extension.network;

import android.net.Uri;

public class ServerConfig {

    private String scheme;
    private final int port;
    private String authority;

    public ServerConfig(String scheme, String authority, int port) {
        this.scheme = scheme;
        this.authority = authority;
        this.port = port;
    }

    public Uri.Builder getUriBuilder() {
        return Uri.parse(scheme + "://" + authority + ":" + port).buildUpon();
    }
}
