package com.hunk.nobank.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.LruCache;
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
        mCache = new ImgCache(maxMemory);
        return instance;
    }

    public Hmg setNetworkBridge(NetworkBridge NetworkBridge) {
        mCache.setNetworkBridge(NetworkBridge);
        return instance;
    }

    public ImgLoadTask load(String url, ImageView imageView) {
        ImgLoadTask task = new ImgLoadTask(url, imageView);
        mCache.load(url, task);
        return task;
    }

    public static class ImgLoadTask {

        private final String mUrl;
        private WeakReference<ImageView> mImgViewRef;

        public ImgLoadTask(String url, ImageView on) {
            this.mUrl = url;
            this.mImgViewRef = new WeakReference<>(on);
        }

        /**
         * Receive base64 string and set it on ImageView
         * @param bitmap
         */
        public void success(Bitmap bitmap) {
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
    }

    public static class ImgCache {
        private LruCache<String, Bitmap> mLruCache;
        private NetworkBridge mNetworkBridge;

        public ImgCache(long maxMemory) {
            mLruCache = new LruCache<>((int) maxMemory / 4);
        }

        /**
         * Find image in cache first, load it from network if we can't find it.
         * @param url
         * @param task
         */
        public void load(final String url, final ImgLoadTask task) {
            Bitmap bitmap = mLruCache.get(url);
            if (bitmap != null) {
                task.success(bitmap);
            } else {
                mNetworkBridge.load(url, new NetworkBridgeCallback() {
                    @Override
                    public void success(String data) {
                        Bitmap bitmap =
                                BitmapFactory.decodeStream(
                                        new Base64InputStream(
                                                new ByteArrayInputStream(
                                                        data.getBytes()), Base64.DEFAULT));
                        mLruCache.put(url, bitmap);
                        task.success(bitmap);
                    }

                    @Override
                    public void fail() {
                    }
                });
            }
        }

        public void setNetworkBridge(NetworkBridge NetworkBridge) {
            this.mNetworkBridge = NetworkBridge;
        }
    }

    /**
     * To adapt network handle, because we want to use our own network lib.
     */
    public interface NetworkBridge {

        void load(String url, NetworkBridgeCallback task);
    }

    /**
     * Network bridge call back
     */
    public interface NetworkBridgeCallback {
        void success(String data);
        void fail();
    }
}
