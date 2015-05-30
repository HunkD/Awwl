package com.hunk.nobank.contract;

import com.google.gson.Gson;

/**
 *
 */
public class ContractGson {

    private static Gson GSON;

    public static synchronized Gson getInstance() {
        if (GSON == null) {
            GSON = prepareGson();
        }
        return GSON;
    }

    private static Gson prepareGson() {
        Gson gson = new Gson();
        return gson;
    }
}
