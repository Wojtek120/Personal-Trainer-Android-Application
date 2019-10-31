package com.wojtek120.personaltrainer.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wojtek120.personaltrainer.R;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/**
 * Configure UniversalImageLoader
 * and get singleton instance of it
 */
@EBean(scope = EBean.Scope.Singleton)
public class ImageLoaderSingleton {

    private static final String TAG = "ImageLoaderBean :: ";
    private static boolean configured = false;
    private static ImageLoader imageLoader;

    @RootContext
    Context context;

    /**
     * Configure ImageLoader
     */
    @AfterInject
    void configure() {
        Log.d(TAG, "Initializing image loader");


        if(!configured) {
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(getImageLoaderConfiguration(context));
            configured = true;
        }

    }

    /**
     * Get ImageLoaderConfiguration
     * @param context - context
     * @return ImageLoaderConfiguration
     */
    private ImageLoaderConfiguration getImageLoaderConfiguration(Context context) {

        int logo = R.drawable.bar_icon;

        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(350))
                .showImageOnLoading(logo)
                .showImageForEmptyUri(logo)
                .showImageOnFail(logo)
                .cacheOnDisk(true).cacheInMemory(true)
                .cacheInMemory(true).resetViewBeforeLoading(true)
                .build();

        return new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(imageOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(1024*1024*100)
                .build();
    }

    /**
     * Throw exception if ImageLoader isn't configured - it needs to be configured before using
     */
    private void checkIfConfigured() {
        if(!configured) {
            throw new IllegalStateException("ImageLoader needs to be configured first");
        }
    }

    /**
     * Load image
     * UniversalImageLoader accepts different URLs
     * for example from web, SD Card or content provider.
     * Function also set visible progressBar if != null and set it gone after loading.
     * Only for static images.
     * @param prefix - stands for setting proper prefix in URL - for example "http://" or "file://"
     * @param URL - URL to image to be loaded
     * @param imageView - ImageView which should display image
     * @param progressBar - ProgressBar which appears at the beginning of loading and disappears when image is loaded
     */
    public void displayImage(String prefix, String URL, ImageView imageView, ProgressBar progressBar) {

        checkIfConfigured();

        imageLoader.displayImage(prefix + URL, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if(progressBar != null) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if(progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if(progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if(progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

}
