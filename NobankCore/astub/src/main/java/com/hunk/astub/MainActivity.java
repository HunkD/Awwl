package com.hunk.astub;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hunkd.annotation.manifest.model.Activity;
import com.hunkd.annotation.manifest.model.IntentFilters;
import com.hunkd.annotation.manifest.model.UsePermission;

import java.io.IOException;

/**
 * Main screen to show application description and a button which to start/pause service
 */
@Activity
@UsePermission(name="android.permission.INTERNET")
@IntentFilters(actions={"android.intent.action.MAIN"},
        categories ={"android.intent.category.LAUNCHER"})
public class MainActivity extends android.app.Activity {

    private static final int SERVICE_PORT = 8466;
    private static final String TAG = "ASTUB";
    private Button mServiceBtn;
    private MyHTTPD mMyHTTPD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMyHTTPD = new MyHTTPD(SERVICE_PORT);
        setupUI();
    }

    private void setupUI() {
        mServiceBtn = (Button) findViewById(R.id.service_btn);
        mServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!mMyHTTPD.isAlive()) {
                        mServiceBtn.setText(R.string.stop_service);
                        mMyHTTPD.start();
                    } else {
                        mServiceBtn.setText(R.string.start_service);
                        mMyHTTPD.stop();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "Main Activity has been destroyed.");
    }
}
