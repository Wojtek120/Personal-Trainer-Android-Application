package com.wojtek120.personaltrainer.utils.adapter;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;


/**
 * Image view - sets height same as width
 */
public class ImageViewSquare extends AppCompatImageView {
    public ImageViewSquare(Context context) {
        super(context);
    }

    public ImageViewSquare(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewSquare(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
