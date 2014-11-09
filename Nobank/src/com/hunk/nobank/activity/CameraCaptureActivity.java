package com.hunk.nobank.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hunk.nobank.R;
import com.hunk.nobank.util.Logging;
import com.hunk.nobank.util.ViewHelper;

/**
 * Description :<br>
 * 
 * @author HunkDeng
 * @since 2014-10-6
 */
public class CameraCaptureActivity extends Activity {

	public final static String INPUT_IMAGE = "com.pactera.phonegapplus.INPUT_IMAGE";
	public final static String RESULT_IMAGE = "com.pactera.phonegapplus.RESULT_IMAGE";
	
	private ImageView mInputImgView;
	private FrameLayout mPreviewLayout;
	private Button mTakeBtn;
	private Button mRetakeBtn;
	private Button mUseItBtn;
	
	private Camera mCamera;
	private CameraPreview mPreview;
	
	private String mInputImage;
	private String mTempImage;
	private Action mState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_surface);
		
		mInputImage = getIntent().getStringExtra(INPUT_IMAGE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setupUI();
		mState = null;
		if (mInputImage == null) {
			openCamera();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		closeCamera();
		if (Action.USE == mState) {
			// check if user pick a new image
			if (mTempImage != null) {
				// then leave it. it already be set in result to caller activity.
			} else {
				// then use the original input image. no operation here.
			}
		} else {
			// user not choose it when back to home. so delete it.
			forceDelete(mTempImage);
		}
	}

	private boolean forceDelete(String imgPath) {
		if (imgPath != null) {
			Log.d(getString(R.string.app_name), "force delete" + imgPath);
			return new File(imgPath).delete();
		}
		return false;
	}

	private void closeCamera() {
		if (mCamera != null) {
			mPreviewLayout.removeView(mPreview);
			mPreview = null;
			releaseCameraAndPreview();
		}
	}

	private void setupUI() {
		findViews();		
		setListeners();
		// 
		if (mInputImage != null) {
			mPreviewLayout.setVisibility(View.GONE);
			mInputImgView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				
				@Override
				public void onGlobalLayout() {
					int reqWidth = mInputImgView.getMeasuredWidth();
					int reqHeight = mInputImgView.getMeasuredHeight();
					try {
						Bitmap bitmap = ViewHelper.decodeSampledBitmapFromResource(CameraCaptureActivity.this, mInputImage, reqWidth, reqHeight);
						if (bitmap != null) {
							mInputImgView.setImageBitmap(bitmap);
						}
					} catch (FileNotFoundException e) {
						Logging.e("");
					}
					mInputImgView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}
			});
			setButtonsEnable(false, true, true);
		} else {
			setButtonsEnable(true, false, false);
		}
	}

	private void setListeners() {
		CaptureBtnListener btnListener = new CaptureBtnListener();
		mTakeBtn.setOnClickListener(btnListener);
		mUseItBtn.setOnClickListener(btnListener);
		mRetakeBtn.setOnClickListener(btnListener);
	}

	private void openCamera() {
		if (checkCameraHardware(this)) {
			if (mCamera == null) {
				mCamera = getCameraInstance();
			}
		} else {
			return;
		}
		mPreview = new CameraPreview(this, mCamera);
		mPreviewLayout.addView(mPreview);
		mPreviewLayout.setVisibility(View.VISIBLE);
	}

	private Camera getCameraInstance() {
		Camera localCamera = null;
		try {
			releaseCameraAndPreview();
			localCamera = Camera.open();
			return localCamera;
		} catch (Exception localException) {
			Log.e(getString(R.string.app_name), "failed to open Camera");
		}
		return localCamera;
	}

	private void releaseCameraAndPreview() {
//		mPreview.setCamera(null);
	    if (mCamera != null) {
	    	mCamera.stopPreview();
	        mCamera.release();
	        mCamera = null;
	    }
	}

	private boolean checkCameraHardware(Context ctx) {
		 if (ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
			// this device has a camera
			 return true;
		 } else {
			 return false;
		 }
	}

	private void findViews() {
		mInputImgView = (ImageView) findViewById(R.id.preivew_id);
		mPreviewLayout = (FrameLayout) findViewById(R.id.surface_preview_layout);

		mTakeBtn = (Button) findViewById(R.id.take_id);
		mRetakeBtn = (Button) findViewById(R.id.retake_id);
		mUseItBtn = (Button) findViewById(R.id.use_id);
	}
	
	@SuppressLint("SimpleDateFormat")
	private String save2External(byte[] data) {
		String str = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		FileOutputStream localFileOutputStream = null;
	    try {
	      mTempImage = ("IMG_" + str + ".jpg");
	      Logging.d("create image file=" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + mTempImage);
	      localFileOutputStream = new FileOutputStream(
	    		  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + mTempImage);
	      localFileOutputStream.write(data);
	      return mTempImage;
	    } catch (IOException e) {
	    	Log.e(getString(R.string.app_name), "" + e.getMessage());
		} finally {
	    	try {
	    		if (localFileOutputStream != null) {
	    			localFileOutputStream.flush();
	    			localFileOutputStream.close();
	    		}
			} catch (IOException e) {
				Log.e(getString(R.string.app_name), "" + e.getMessage());
			}     
	    }
		return null;
	}

	@SuppressLint("SimpleDateFormat")
	private String save2Internal(byte[] data) {
		String str = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		FileOutputStream localFileOutputStream = null;
	    try {
	      mTempImage = ("IMG_" + str + ".jpg");
	      Log.d(getString(R.string.app_name), "create image file=" + mTempImage);
	      localFileOutputStream = openFileOutput(mTempImage, 0);
	      localFileOutputStream.write(data);
	      return mTempImage;
	    } catch (IOException e) {
	    	Log.e(getString(R.string.app_name), "" + e.getMessage());
		} finally {
	    	try {
	    		if (localFileOutputStream != null) {
	    			localFileOutputStream.flush();
	    			localFileOutputStream.close();
	    		}
			} catch (IOException e) {
				Log.e(getString(R.string.app_name), "" + e.getMessage());
			}     
	    }
		return null;
	}
	
	private void setDesiredCameraParameters() {
		Camera.Parameters cameraParams = this.mCamera.getParameters();
	    if (cameraParams == null) {
	    	Log.d(getString(R.string.app_name), "Device error: no camera parameters are available. Proceeding without configuration.");
	    	return;
	    }
	    Point bestPreviewSize = getPreviewSize(cameraParams);
	    Log.d(getString(R.string.app_name), "preview size: " + bestPreviewSize.x + "  " + bestPreviewSize.y);
	    cameraParams.setPreviewSize(bestPreviewSize.x, bestPreviewSize.y);
	    Point pictureSize = getCameraPictureSize(cameraParams, bestPreviewSize);
	    Log.d(getString(R.string.app_name), "picture size: " + pictureSize.x + "  " + pictureSize.y);
	    cameraParams.setPictureSize(pictureSize.x, pictureSize.y);
	    cameraParams.setPictureFormat(256);
	    cameraParams.setJpegQuality(100);
	    cameraParams.setFocusMode("auto");
	    try {
	    	this.mCamera.setParameters(cameraParams);
	    	return;
	    } catch (RuntimeException localRuntimeException) {
	    	Log.d(getString(R.string.app_name), "Failed for camera setParameters" + localRuntimeException.getMessage());
	    }
	}

	private Point getCameraPictureSize(Parameters localParameters, Point limited) {
		List<Size> localList = localParameters.getSupportedPictureSizes();
		return findBestSolution(localList, limited);
	}

	private Point getPreviewSize(Parameters params) {
		Display localDisplay = ((WindowManager)getSystemService("window")).getDefaultDisplay();
	    
	    Point point = new Point(localDisplay.getWidth(), localDisplay.getHeight());
	    if (point.x < point.y) {
	    	Log.d(getString(R.string.app_name), "Display reports portrait orientation; assuming this is incorrect");
	    	int k = point.x;
	    	point.x = point.y;
	    	point.y = k;
	    }
	    Log.d(getString(R.string.app_name), "" + point.x + "  " + point.y);
	    return findBestPreviewSizeValue(params, point);
	}

	private Point findBestSolution(List<Size> supported, Point limited) {
		Camera.Size result = null;
		
		for (Size size : supported) {
			if (size.width <= limited.x && size.height <= limited.y) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;

					if (newArea > resultArea) {
						result = size;
					}
				}
			}
		}
		return new Point(result.width, result.height);
	}
	
	private Point findBestPreviewSizeValue(Parameters params, Point point) {
		List<Size> localList = params.getSupportedPreviewSizes();
		return findBestSolution(localList, point);
	}

	private void setButtonsEnable(boolean b, boolean c, boolean d) {
		mTakeBtn.setEnabled(b);
		setBtnVisible(b, mTakeBtn);
		mUseItBtn.setEnabled(c);
		setBtnVisible(c, mUseItBtn);
		mRetakeBtn.setEnabled(d);
		setBtnVisible(d, mRetakeBtn);
	}
	
	private void setBtnVisible(boolean visible, Button mTakeBtn) {
		if (visible) {
			mTakeBtn.setVisibility(View.VISIBLE);
		} else {
			mTakeBtn.setVisibility(View.GONE);
		}
	}
	
	private class CameraPreview extends SurfaceView implements
			SurfaceHolder.Callback {
		private SurfaceHolder mHolder;
		private Camera mCamera;
		
		@SuppressWarnings("deprecation")
		public CameraPreview(Context context, Camera camera) {
			super(context);
			this.mCamera = camera;
			
			mHolder = getHolder();
			mHolder.addCallback(this);
			// deprecated setting, but required on Android versions prior to 3.0
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// The Surface has been created, now tell the camera where to draw the preview.
			try {
				setDesiredCameraParameters();
				mCamera.setPreviewDisplay(holder);
				startPreview();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// empty. Take care of releasing the Camera preview in your
			// activity.
		}
		
		public void startPreview() {
			if (mCamera != null) {
				mCamera.startPreview();
			}
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int w,
				int h) {
			// If your preview can change or rotate, take care of those events here.
			// Make sure to stop the preview before resizing or reformatting it.
			if (mHolder.getSurface() == null) {
				// preview surface does not exist
				return;
			}

			// stop preview before making changes
			try {
				mCamera.stopPreview();
			} catch (Exception e) {
				// ignore: tried to stop a non-existent preview
			}
			// make any resize, rotate or reformatting changes here
			// start preview with new settings
			surfaceCreated(holder);
		}
	}
	
	private class CaptureBtnListener implements View.OnClickListener {
		
		private AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {

			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				mCamera.takePicture(null, null, mPictureCallback);
			}
		};
		
		private PictureCallback mPictureCallback = new PictureCallback() {
			
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				// SamsungNote2 need this, but we can safely call it each time after capture picture.
				mCamera.stopPreview();
				//
				Bitmap bitmap = 
						BitmapFactory.decodeByteArray(data, 0, data.length);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
				bitmap.recycle();
		
				byte[] _data = baos.toByteArray();
				save2Internal(_data);
				save2External(_data);
			}
			
		};

		@Override
		public void onClick(View v) {
			if (v.getId() == mTakeBtn.getId()) {
				uiActionOfSourceType(Action.TAKE);
				mCamera.autoFocus(autoFocusCallback);
			} else if (v.getId() == mRetakeBtn.getId()){
				uiActionOfSourceType(Action.RETAKE);
				forceDelete(mTempImage);
				// When user set an input image and want to replace it with new capture photo,
				// the camera has not started after user click retake button.
				if (mCamera == null) { 
					openCamera();
				} else {
					mPreview.startPreview();
				}
				
				mState = null;
			} else if (v.getId() == mUseItBtn.getId()) {
				if (mState == null) {
					getIntent().putExtra(RESULT_IMAGE, mInputImage);
				} else {
					getIntent().putExtra(RESULT_IMAGE, mTempImage);
				}
				uiActionOfSourceType(Action.USE);
				
				setResult(RESULT_OK, getIntent());
				Logging.d("mTempImage = " + mTempImage);
				finish();
			}
		}
		
		private void uiActionOfSourceType(Action take) {
			mState = take;
			switch (take) {
			case TAKE:
				setButtonsEnable(false, true, true);
				break;
			case RETAKE:
				setButtonsEnable(true, false, false);
				break;
			case USE:
				setButtonsEnable(false, true, true);
				break;
			default:
				break;
			}
		}
	}
	
	private enum Action {
		TAKE, USE, RETAKE
	}
}
