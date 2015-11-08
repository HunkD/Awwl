package com.hunk.nobank.activity.settings;

import android.os.Bundle;

import com.hunk.nobank.R;
import com.hunk.nobank.activity.BaseActivity;

/**
 *
 */
public class SettingsHomeActivity extends BaseActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_home_page, Base.NORMAL);
        getTitleBarPoxy().getTitle().setText(R.string.settings);
    }
}
