package com.hunk.nobank.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;

import com.hunk.nobank.R;
import com.hunk.abcd.extension.log.Logging;
import com.hunk.nobank.views.CameraPreview;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description :<br>
 *
 * @author HunkDeng
 * @since 2014-10-6
 */
public class CameraCaptureActivity extends Activity {

    public final static String RESULT_IMAGE = "com.pactera.phonegapplus.RESULT_IMAGE";

    private Camera mCamera;
    private CameraPreview mPreview;

    private FrameLayout mPreviewLayout;
    private View mLeftBtn;
    private View mRightBtn;
    private View mSurface;

    private String mTempImage;
    private State screenState;

    protected Point previewSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_surface);
        setupUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        openCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeCamera();
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
        mPreviewLayout = (FrameLayout) findViewById(R.id.surface_preview_layout);
        mLeftBtn = findViewById(R.id.surface_preview_left_btn);
        mRightBtn = findViewById(R.id.surface_preview_right_btn);
        mSurface = findViewById(R.id.surface_focus_area);
        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.surface_preview_left_btn) {
                    screenState.leftButtonAction();
                } else if (v.getId() == R.id.surface_preview_right_btn) {
                    screenState.rightButtonAction();
                }
            }
        };
        mLeftBtn.setOnClickListener(onClickListener);
        mRightBtn.setOnClickListener(onClickListener);
    }

    private void openCamera() {
        if (checkCameraHardware(this)) {
            if (mCamera == null) {
                mCamera = getCameraInstance();
            }
        } else {
            return;
        }
        if (previewSize == null) {
            mPreviewLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        mPreviewLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        mPreviewLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    previewSize = getPreviewSize();
                    Logging.d("previewSize = " + previewSize.x + "," + previewSize.y);
                    mPreview = new CameraPreview(CameraCaptureActivity.this, mCamera);
                    mPreview.setPreviewSize(previewSize);
                    mPreviewLayout.addView(mPreview);

                    screenState = new PreviewState();
                }
            });
        } else {
            mPreview = new CameraPreview(CameraCaptureActivity.this, mCamera);
            mPreview.setPreviewSize(previewSize);
            mPreviewLayout.addView(mPreview);

            screenState = new PreviewState();
        }
    }

    private Point getPreviewSize() {
        if (mPreviewLayout.getMeasuredWidth() == 0) {
            mPreviewLayout.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        }

        return new Point(mPreviewLayout.getMeasuredWidth(), mPreviewLayout.getMeasuredHeight());
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
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private boolean checkCameraHardware(Context ctx) {
        if (ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            return false;
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void save2External(byte[] data) {
        String str = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        FileOutputStream localFileOutputStream = null;
        try {
            Logging.d("create image file=" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + ("IMG_" + str + ".jpg"));
            localFileOutputStream = new FileOutputStream(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + mTempImage);
            localFileOutputStream.write(data);
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

    private interface State {
        void leftButtonAction();

        void rightButtonAction();
    }

    private class PreviewState implements State {
        private AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {

            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                mCamera.takePicture(null, null, mPictureCallback);
            }
        };

        private PictureCallback mPictureCallback = new PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                // SamsungNote2 need this, but we can safely call it each time
                // after capture picture.
//				mCamera.stopPreview();
                screenState = new ConfirmState();
                //
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
                        data.length);
                bitmap = cropImage(bitmap);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                bitmap.recycle();

                byte[] _data = baos.toByteArray();
                // TODO Auto-generated method stub
                save2Internal(_data);
                save2External(_data);

            }

            private Bitmap cropImage(Bitmap bitmap) {
                Point previewSize = getPreviewSize();
                Logging.d("previewSize : " + previewSize.x + ", " + previewSize.y);
                Point viewSize = getViewSize();
                Logging.d("viewSize : " + viewSize.x + ", " + viewSize.y);
                Point picSize = getPictureSize();

                float widthPect = (float) viewSize.x / (float) previewSize.x;
                float hightPect = (float) viewSize.y / (float) previewSize.y;

                Point cropPicSize = new Point((int) (picSize.x * widthPect), (int) (picSize.y * hightPect));
                int x = (picSize.x - cropPicSize.x) / 2;
                int y = (picSize.y - cropPicSize.y) / 2;
                return Bitmap.createBitmap(bitmap, x, y, cropPicSize.x, cropPicSize.y);
            }

            private Point getPreviewSize() {
                return new Point(mPreviewLayout.getMeasuredWidth(), mPreviewLayout.getMeasuredHeight());
            }

            private Point getPictureSize() {
                return mPreview.getPictureSize();
            }

            private Point getViewSize() {
                return new Point(mSurface.getMeasuredWidth(), mSurface.getMeasuredHeight());
            }
        };

        public PreviewState() {
            mLeftBtn.setBackgroundResource(R.drawable.multiplication);
            mRightBtn.setBackgroundResource(R.drawable.camera);

            mPreview.startPreview();
        }

        @Override
        public void leftButtonAction() {
            finish();
        }

        @Override
        public void rightButtonAction() {
            mCamera.autoFocus(autoFocusCallback);
        }
    }

    private class ConfirmState implements State {

        public ConfirmState() {
            mLeftBtn.setBackgroundResource(R.drawable.multiplication);
            mRightBtn.setBackgroundResource(R.drawable.check);

            mCamera.stopPreview();
        }

        @Override
        public void leftButtonAction() {
            // delete temporary file
            forceDelete(mTempImage);
            // restart preview
            screenState = new PreviewState();
        }

        @Override
        public void rightButtonAction() {
            Intent result = new Intent();
            result.putExtra(RESULT_IMAGE, mTempImage);
            setResult(Activity.RESULT_OK, result);
            mTempImage = null;
            finish();
        }
    }
}
