package com.hunk.nobank.feature.base.activity;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hunk.nobank.R;

public class DashboardActivity extends AccountBaseActivity {
	final int CAMERA_REQUEST = 30;
	private ImageView mCaptured;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard_page);
		
		setupUI();
	}

	private void setupUI() {
		findViewById(R.id.capture).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String packageName = getApplication().getPackageName();
				
				Intent gotoCapture = new Intent();
				gotoCapture.setAction(packageName + ".action.goto.base.capture.picture");
				startActivityForResult(gotoCapture, CAMERA_REQUEST);
			}
		});
		
		mCaptured = (ImageView) findViewById(R.id.captured);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST) {
			if (resultCode == Activity.RESULT_OK) {
				String path = data.getStringExtra(CameraCaptureActivity.RESULT_IMAGE);
				BitmapFactory.Options options = new BitmapFactory.Options();
				Bitmap img;
				try {
					img = BitmapFactory.decodeStream(
							openFileInput(path), null,
							options);
					mCaptured.setImageBitmap(img);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}