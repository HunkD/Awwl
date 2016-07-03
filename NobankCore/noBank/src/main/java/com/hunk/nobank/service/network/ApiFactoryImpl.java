package com.hunk.nobank.service.network;

import com.hunk.nobank.contract.ContractGson;
import com.hunk.nobank.extension.network.ServerConfig;

import java.util.HashMap;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author HunkDeng
 * @since 2016/7/3
 */
public class ApiFactoryImpl implements ApiFactory {
    private final Retrofit mRetrofit;
    private HashMap<Class<?>, Object> mApiMap = new HashMap<>();

    public ApiFactoryImpl(ServerConfig currentServerConfig) {
        // Retrofit init
        mRetrofit = new Retrofit.Builder()
                .baseUrl(currentServerConfig.getUriBuilder().build().toString())
                .addConverterFactory(
                        GsonConverterFactory.create(ContractGson.getInstance()))
                .build();
    }

    @Override
    public <T> T get(Class<T> apiClazz) {
        T api = null;
        if (mApiMap.containsKey(apiClazz)) {
            Object _api = mApiMap.get(apiClazz);
            if (_api != null) {
                if (_api.getClass().equals(apiClazz)) {
                    api = (T) _api;
                }
            }
        }
        if (api == null) {
            api = mRetrofit.create(apiClazz);
            mApiMap.put(apiClazz, api);
        }
        return api;
    }
}
