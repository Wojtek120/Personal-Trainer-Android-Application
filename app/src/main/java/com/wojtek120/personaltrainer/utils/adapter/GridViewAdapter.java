package com.wojtek120.personaltrainer.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.utils.ImageLoaderSingleton;

import java.util.ArrayList;

import lombok.Builder;


/**
 * Build in ViewHolder pattern,
 * used to show images
 */
public class GridViewAdapter extends ArrayAdapter<String> {

    /**
     * ViewHolder pattern
     * similar to recycle view
     */
    @Builder
    private static class ViewHolder {
        ImageViewSquare image;
        ProgressBar progressBar;
    }


    private int resource;
    private LayoutInflater layoutInflater;
    private String filePrefix;
    private ImageLoaderSingleton imageLoaderSingleton;

    public GridViewAdapter(Context context, ArrayList<String> imagePaths, int resource, String filePrefix, ImageLoaderSingleton imageLoaderSingleton) {
        super(context, resource, imagePaths);
        this.resource = resource;
        this.filePrefix = filePrefix;
        this.imageLoaderSingleton = imageLoaderSingleton;

        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(resource, parent, false);

            viewHolder = ViewHolder.builder()
                    .image(convertView.findViewById(R.id.imageViewInGrid))
                    .progressBar(convertView.findViewById(R.id.progressBarInGrid))
                    .build();

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String imagePath = getItem(position);

        imageLoaderSingleton.displayImage(filePrefix, imagePath, viewHolder.image, viewHolder.progressBar);

        return convertView;
    }
}
