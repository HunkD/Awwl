package com.hunk.nobank.activity.settings;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hunk.nobank.NConstants;
import com.hunk.nobank.R;
import com.hunk.nobank.activity.BaseActivity;
import com.hunk.whitelabel.Feature;

/**
 *
 */
public class SettingsHomeActivity extends BaseActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_home_page, Base.NORMAL);
        getTitleBarPoxy().getTitle().setText(R.string.settings);

        setupUI();
    }

    private void setupUI() {
        findViewById(R.id.personal_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toScreenIntent = new Intent();
                toScreenIntent.setPackage(getPackageName());
                toScreenIntent.setAction(
                        BaseActivity.generateAction(Feature.personal_info, NConstants.OPEN_MAIN));
                toScreenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PackageManager pm = getPackageManager();
                if (pm.resolveActivity(toScreenIntent, 0) != null) {
                    startActivity(toScreenIntent);
                } else {
                    Toast.makeText(SettingsHomeActivity.this, "Umm, it seems we had trouble here.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
