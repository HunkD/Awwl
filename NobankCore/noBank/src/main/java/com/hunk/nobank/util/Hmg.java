package com.hunk.nobank.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Base64InputStream;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.lang.ref.WeakReference;

/**
 * Hunk Image loader
 * 1. load image from network with okhttp or other network lib.
 * 2. load image to lru cache.
 * 3. load image to image view, will do optimize/scale image before loading it.
 * 4. support pause loading operation.
 * 5. release/clean image cache and image view reference.
 *
 * @author HunkDeng
 * @since 2016/6/19
 */
public class Hmg {
    private static Hmg instance;
    private ImgCache mCache;

    private Hmg() {}
    public static Hmg getInstance() {
        if (instance == null) {
            instance = new Hmg();
        }
        return instance;
    }

    public Hmg init(Context context) {
        Runtime rt = Runtime.getRuntime();
        long maxMemory = rt.maxMemory();
        Logging.d("maxMemory:" + Long.toString(maxMemory));
        mCache = new ImgCache();
        mCache.setMaxMemory(maxMemory);
        return instance;
    }

    public Hmg setNetworkBridge(NetworkBridge NetworkBridge) {
        mCache.setNetworkBridge(NetworkBridge);
        return instance;
    }

    public ImgLoadTask load(String url) {
        ImgLoadTask task = new ImgLoadTask(url);
        mCache.load(url, task);
        return task;
    }

    public static class ImgLoadTask {

        private final String mUrl;
        private WeakReference<ImageView> mImgViewRef;

        public ImgLoadTask(String url) {
            this.mUrl = url;
        }

        public void setOn(ImageView on) {
            mImgViewRef = new WeakReference<>(on);
        }

        /**
         * Receive base64 string and set it on ImageView
         * @param data
         */
        public void success(String data) {
            // decode base64 data
            Bitmap bitmap = BitmapFactory.decodeStream(new Base64InputStream(new ByteArrayInputStream(data.getBytes()), Base64.DEFAULT));
            ImageView imageView = mImgViewRef.get();
            if (imageView != null) {
                // since list view has recycler mechanism, view will reused.
                // This may set wrong image to image view we hold when after scroll
                if (mUrl != null && imageView.getTag().equals(mUrl)) {
                    imageView.setImageBitmap(bitmap);
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        }

        public void fail() {

        }
    }

    public static class ImgCache {

        private NetworkBridge mNetworkBridge;

        public void setMaxMemory(long maxMemory) {

        }

        public void load(String url, ImgLoadTask task) {
            mNetworkBridge.load(url, task);
        }

        public void setNetworkBridge(NetworkBridge NetworkBridge) {
            this.mNetworkBridge = NetworkBridge;
        }
    }

    public interface NetworkBridge {

        void load(String url, ImgLoadTask task);
    }
}
