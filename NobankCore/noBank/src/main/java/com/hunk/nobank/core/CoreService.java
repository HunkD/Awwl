package com.hunk.nobank.core;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class CoreService extends IntentService {

    public static final String WORK_THREAD_NAME = CoreService.class.getSimpleName();

    public static final String REQUEST_DATA = "REQUEST_DATA";
    public static final String REQUEST_RECEIVER = "REQUEST_RECEIVER";
    public static final String RESULT_ACTION = "RESULT_ACTION";
    public static final String RESULT_DATA = "RESULT_DATA";

    private static Gson GSON = null;

    public static Map<String, FeatureManager> mRegisteredFeatureManager = new HashMap<>();

    public CoreService() {
        super(WORK_THREAD_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // parse request
        String action = intent.getAction();
        String[] actionStrArr = action.split("\\.");
        String featureCategory = actionStrArr[1];   // featureCategory : Feature.XXXX.toString()
        String realAction = actionStrArr[2];
        String data = intent.getStringExtra(REQUEST_DATA);
        ResultReceiver receiver = intent.getParcelableExtra(REQUEST_RECEIVER);
        // Prepare result
        Bundle bundle = new Bundle();
        bundle.putString(RESULT_ACTION, action);
        // Find detail feature manager to handle the request.
        FeatureManager manager = mRegisteredFeatureManager.get(featureCategory);
        if (manager != null) {
            // Let the detail feature manager to handle this request.
            String result = manager.onHandle(realAction, data);
            bundle.putString(RESULT_DATA, result);
        } else {
            // Response null to caller that requested feature manager is not exist.
        }
        receiver.send(0, bundle);
    }

    public static void startCoreService(Context context, String action, Object reqData, ResultReceiver mReceiver) {
        Intent intent = new Intent(context, CoreService.class);
        intent.setAction(action);
        intent.putExtra(REQUEST_DATA, getGson(reqData));
        intent.putExtra(REQUEST_RECEIVER, mReceiver);
        context.startService(intent);
    }

    public static String getGson(Object obj) {
        if (GSON == null) {
            GSON = new Gson();
        }
        return GSON.toJson(obj);
    }
}
