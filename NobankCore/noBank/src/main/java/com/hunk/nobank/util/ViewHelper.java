package com.hunk.nobank.util;

import java.io.FileNotFoundException;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class ViewHelper {
	public static void setUninteractable(View paramView) {
		paramView.setClickable(false);
		paramView.setFocusable(false);
		paramView.setFocusableInTouchMode(false);
	}
	
	public static Bitmap decodeSampledBitmapFromResource(Context context, String filePath,
	        int reqWidth, int reqHeight) throws FileNotFoundException {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    
	    BitmapFactory.decodeStream(
	    		context.openFileInput(filePath), null,
	    		options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//	    options.inSampleSize = 4;
	    
	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeStream(
	    		context.openFileInput(filePath), null,
	    		options);
	}

	private static int calculateInSampleSize(Options options, int reqWidth,
			int reqHeight) {
		// Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {

	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;

	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) > reqHeight
	                && (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }

	    return inSampleSize;
	}

	public static void showDownSizeImage(Context context, ImageView mInputImgView, String mInputImage, int i) {
		Bitmap localBitmap = getDownSizeBitmap(context,
				mInputImage, i);
		if (localBitmap != null) {
			mInputImgView.setImageBitmap(localBitmap);
		}
	}

	public static Bitmap getDownSizeBitmap(Context ctx, String paramString, int paramInt) {
		try {
			BitmapFactory.Options localOptions = new BitmapFactory.Options();
			localOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(
					ctx.openFileInput(paramString), null,
					localOptions);
			localOptions.inSampleSize = 1;
			for (float f = localOptions.outWidth * localOptions.outHeight
					/ paramInt; f > 2.0F; f /= 4.0F) {
				localOptions.inSampleSize = (2 * localOptions.inSampleSize);
			}
			localOptions.inJustDecodeBounds = false;
			Bitmap localBitmap = BitmapFactory.decodeStream(
					ctx.openFileInput(paramString), null,
					localOptions);
			return localBitmap;
		} catch (FileNotFoundException localFileNotFoundException) {
			localFileNotFoundException.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param ctx
	 * @param config
	 * 			Configuration.ORIENTATION_LANDSCAPE | Configuration.ORIENTATION_PORTRAIT
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static Point getScreenSize(Context ctx, int config) {
		Display display = ((WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Point point = new Point();
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
	    	display.getSize(point);
	    } else {
	    	point.x = display.getWidth();
	    	point.y = display.getHeight();
	    }
	    boolean needSwitchEachOther = false;
	    if (config == Configuration.ORIENTATION_LANDSCAPE) {
	    	if (point.x < point.y) {
	    		needSwitchEachOther = true;
	    	}
	    } else if (config == Configuration.ORIENTATION_PORTRAIT){
	    	if (point.x < point.y) {
	    		needSwitchEachOther = true;
	    	}
	    }
	    
	    if (needSwitchEachOther) {
	    	int k = point.x;
	    	point.x = point.y;
	    	point.y = k;
	    }
	    return point;
	}

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void translateX(final View mCardNumberInput, final float src, final float length,
                                  int animationDurationMedium) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ObjectAnimator.ofFloat(mCardNumberInput, "translationX", length)
                    .setDuration(2000).start();
        } else {
            TranslateAnimation ta = new TranslateAnimation(
                    src,
                    length,
                    0, 0);
            ta.setDuration(animationDurationMedium);
            ta.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationRepeat(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    mCardNumberInput.setTranslationX(length);
                }
            });
            mCardNumberInput.startAnimation(ta);
        }
    }
}
