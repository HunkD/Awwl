package com.hunk.nobank.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;

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
    private HashSet<String> mLastTimeFetchList = new HashSet<>();

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

    public void load(String url, ImageView imageView, @DrawableRes int imgDefault) {
        if (url != null) {
            Bitmap bitmap = mCache.get(url);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(imgDefault);
                imageView.setTag(url);
            }
        }
    }

    public ImgLoadTask load(String url, ImageView imageView) {
        if (StringUtils.isNullOrEmpty(url)) {
            imageView.setTag(null);
            return null;
        }
        imageView.setTag(url);
        ImgLoadTask task = new ImgLoadTask(url, imageView);
        mCache.load(url, task);
        return task;
    }

    /**
     * 1. Extract urls from full url list.
     * 2. find them in lru cache.
     * 3. load them if there's no cache.
     * @param mUrlList
     * @param first
     * @param end
     * @param successCallBack
     */
    public void loadList(List<String> mUrlList, int first, int end, final SuccessCallBack successCallBack) {
        for (int i = first; i <= end; i ++) {
            if (i >= mUrlList.size()) {
                return;
            }
            final String url = mUrlList.get(i);
            if (url != null) {
                Bitmap bitmap = mCache.get(url);
                if (bitmap == null) {
                    /**
                     * if (bitmap != null) we can't directly call successCallBack(url, bitmap) here.
                     * it will create bug, since it will overwrite the image we set in getView() method.
                     */
                    if (!mLastTimeFetchList.contains(url)) {
                        mLastTimeFetchList.add(url);
                        mCache.loadList(url, new SuccessCallBack() {
                            @Override
                            public void notify(String url, Bitmap bitmap) {
                                successCallBack.notify(url, bitmap);
                                mLastTimeFetchList.remove(url);
                            }
                        });
                    }
                }
            }
        }
    }

    public void cancelLastTimeListFetch() {

    }

    public void clear() {
        mCache.mLruCache.evictAll();
    }

    public interface SuccessCallBack {
        void notify(String url, Bitmap bitmap);
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
            mLruCache = new LruCache<String, Bitmap>((int) maxMemory / 4) {
                @SuppressLint("NewApi")
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount();
                }
            };
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

        public Bitmap get(String url) {
            return mLruCache.get(url);
        }

        public void loadList(final String url, final SuccessCallBack successCallBack) {
            mNetworkBridge.load(url, new NetworkBridgeCallback() {
                @Override
                public void success(String data) {
                    Bitmap bitmap =
                            BitmapFactory.decodeStream(
                                    new Base64InputStream(
                                            new ByteArrayInputStream(
                                                    data.getBytes()), Base64.DEFAULT));
                    mLruCache.put(url, bitmap);
                    successCallBack.notify(url, bitmap);
                }

                @Override
                public void fail() {
                }
            });
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
