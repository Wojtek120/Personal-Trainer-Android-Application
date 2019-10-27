package com.wojtek120.personaltrainer.utils;

import android.content.Context;
import android.graphics.Bitmap;
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

/**
 * Configure UniversalImageLoader
 * and get singleton instance of it
 */
public class ImageLoaderSingleton {

    private static boolean configured = false;
    private static ImageLoader imageLoader;

    private ImageLoaderSingleton() {
    }

    /**
     * Configure ImageLoader
     * @param context - context
     */
    public static void configure(Context context) {

        if(!configured) {
            ImageLoader.getInstance().init(getImageLoaderConfiguration(context));
            configured = true;
        }

    }

    /**
     * Get ImageLoaderConfiguration
     * @param context - context
     * @return ImageLoaderConfiguration
     */
    private static ImageLoaderConfiguration getImageLoaderConfiguration(Context context) {

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
    private static void checkIfConfigured() {
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
    public static void displayImage(String prefix, String URL, ImageView imageView, ProgressBar progressBar) {

        checkIfConfigured();

        ImageLoader imageLoader = ImageLoader.getInstance();
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
