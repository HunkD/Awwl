package com.hunk.nobank.views;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hunk.abcd.extension.log.Logging;
import com.hunk.nobank.util.ViewHelper;

import java.io.IOException;
import java.util.List;

/**
 * Description :<br>
 *
 * @author HunkDeng
 * @since 2014-12-14
 */
public class CameraPreview extends SurfaceView implements
        SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Point pictureSize;
    private Point physicalPreviewSize;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        this.mCamera = camera;

        mHolder = getHolder();
        mHolder.addCallback(this);
        setType(mHolder);
    }

    @SuppressWarnings("deprecation")
    private void setType(SurfaceHolder mHolder2) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the
        // preview.
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

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
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

    private void setDesiredCameraParameters() {
        Camera.Parameters cameraParams = this.mCamera.getParameters();
        if (cameraParams == null) {
            Logging.d("Device error: no camera parameters are available. Proceeding without configuration.");
            return;
        }
        Point bestPreviewSize = getPreviewSize(cameraParams);
        Logging.d("preview size: " + bestPreviewSize.x + "  " + bestPreviewSize.y);
        cameraParams.setPreviewSize(bestPreviewSize.x, bestPreviewSize.y);
        pictureSize = getCameraPictureSize(cameraParams, bestPreviewSize);
        Logging.d("picture size: " + pictureSize.x + "  " + pictureSize.y);
        cameraParams.setPictureSize(pictureSize.x, pictureSize.y);
        cameraParams.setPictureFormat(256);
        cameraParams.setJpegQuality(100);
        cameraParams.setFocusMode("auto");
        try {
            this.mCamera.setParameters(cameraParams);
            return;
        } catch (RuntimeException localRuntimeException) {
            Logging.d("Failed for camera setParameters" + localRuntimeException.getMessage());
        }
    }

    private Point getCameraPictureSize(Parameters localParameters, Point limited) {
        List<Size> localList = localParameters.getSupportedPictureSizes();
        return findBestSolution(localList, limited);
    }

    private Point getPreviewSize(Parameters params) {
        Point _physicalPreviewSize = physicalPreviewSize != null ? physicalPreviewSize : ViewHelper.getScreenSize(getContext(), Configuration.ORIENTATION_LANDSCAPE);
        Logging.d(_physicalPreviewSize.x + "  " + _physicalPreviewSize.y);
        return findBestSolution(params.getSupportedPreviewSizes(), _physicalPreviewSize);
    }

    private Point findBestSolution(List<Size> supported, Point limited) {
        Point bestSolutionP = new Point();
        float bestSolution = 1000000;
        for (Size size : supported) {
            Point _size = new Point();
            if (size.width < size.height) {
                _size.x = size.height;
                _size.y = size.width;
            } else {
                _size.x = size.width;
                _size.y = size.height;
            }

            float newSolution = (float) limited.x / (float) limited.y - (float) _size.x / (float) _size.y;
            if (newSolution < bestSolution) {
                bestSolutionP.x = _size.x;
                bestSolutionP.y = _size.y;
                bestSolution = newSolution;
            } else if (newSolution == 0) {
                return _size;
            }
        }
        return bestSolutionP;
    }

    public Camera getmCamera() {
        return mCamera;
    }

    public Point getPictureSize() {
        return pictureSize;
    }

    public void setPreviewSize(Point physicalPreviewSize) {
        this.physicalPreviewSize = physicalPreviewSize;
    }
}
