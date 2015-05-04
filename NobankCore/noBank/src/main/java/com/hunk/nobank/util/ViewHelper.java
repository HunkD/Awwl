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
import android.graphics.Typeface;
import android.os.Build;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hunk.nobank.NoBankApplication;
import com.hunk.nobank.R;

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

    public static void translateX(final View mCardNumberInput, final float src, final float length,
                                  int animationDurationMedium) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ObjectAnimator.ofFloat(mCardNumberInput, "translationX", mCardNumberInput.getTranslationX() + length)
                    .setDuration(2000).start();
        } else {
            AnimationSet set = new AnimationSet(true);
            TranslateAnimation ta = new TranslateAnimation(
                    src - length,
                    src,
                    0, 0);
            ta.setDuration(animationDurationMedium);
            set.addAnimation(ta);
            set.setFillAfter(true);
            mCardNumberInput.offsetLeftAndRight((int)length);
            mCardNumberInput.startAnimation(set);
        }
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final int dimen) {
        float dp = context.getResources().getDimension(dimen);
        return pxFromDp(context, dp);
    }

    public static String actionToString(int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN: return "Down";
            case MotionEvent.ACTION_MOVE: return "Move";
            case MotionEvent.ACTION_POINTER_DOWN: return "Pointer Down";
            case MotionEvent.ACTION_UP: return "Up";
            case MotionEvent.ACTION_POINTER_UP: return "Pointer Up";
            case MotionEvent.ACTION_OUTSIDE: return "Outside";
            case MotionEvent.ACTION_CANCEL: return "Cancel";
        }
        return "";
    }

    public static void updateFontsStyle(ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View v = vg.getChildAt(i);
            if (v instanceof TextView) {
                setFontByConfig((TextView)v);
            } else if (v instanceof ViewGroup) {
                updateFontsStyle((ViewGroup)v);
            }
        }
    }

    private static void setFontByConfig(TextView tv) {
        TypefaceCache cache = NoBankApplication.getInstance().getTypefaceCache();
        int style = tv.getTypeface().getStyle();
        if (style == Typeface.BOLD) {
            tv.setTypeface(cache.BOLD);
        } else if (style == Typeface.ITALIC){
            tv.setTypeface(cache.ITALIC);
        } else if (style == Typeface.NORMAL) {
            tv.setTypeface(cache.NORMAL);
        }
    }

    public static class TypefaceCache {
        private static TypefaceCache instance;
        public final Typeface NORMAL;
        public final Typeface ITALIC;
        public final Typeface BOLD;

        private TypefaceCache(Context ctx) {
            NORMAL = Typeface.createFromAsset(ctx.getAssets(),"fonts/Roboto-Light.ttf");
            ITALIC = Typeface.createFromAsset(ctx.getAssets(),"fonts/Roboto-LightItalic.ttf");
            BOLD = Typeface.createFromAsset(ctx.getAssets(),"fonts/Roboto-Regular.ttf");
        }

        public static synchronized TypefaceCache getInstance(Context ctx) {
            if (instance == null) {
                instance = new TypefaceCache(ctx);
            }
            return instance;
        }
    }
}
