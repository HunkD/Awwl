package com.hunk.astub;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

/**
 * Main screen to show application description and a button which to start/pause service
 */
public class MainActivity extends Activity {

    private static final int SERVICE_PORT = 8451;
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
}