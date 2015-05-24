package com.hunk.nobank.extension.network;

public class ServerConfig {
    private final int port;
    private String url;

    public ServerConfig(String url, int port) {
        this.url = url;
        this.port = port;
    }

    public String getURL() {
        return url + ":" + port;
    }
}
