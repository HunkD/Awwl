package com.hunk.nobank.contract;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hunk.nobank.contract.adapter.DateAdapter;
import com.hunk.nobank.contract.adapter.MoneyAdapter;

import java.util.Date;

/**
 * Get Gson instance
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
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateAdapter())
                .registerTypeAdapter(Money.class, new MoneyAdapter())
                .create();

        return gson;
    }
}
