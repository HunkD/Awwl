package com.hunk.nobank.util;

import java.io.FileNotFoundException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.view.View;
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
}
