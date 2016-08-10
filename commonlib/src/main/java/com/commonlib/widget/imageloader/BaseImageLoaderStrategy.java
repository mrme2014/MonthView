package com.commonlib.widget.imageloader;

import android.content.Context;

/**
 * Created by wqf on 16/8/10.
 * abstract class/interface defined to load image
 * (Strategy Pattern used here)
 */
public interface BaseImageLoaderStrategy {
    void loadImage(Context ctx, ImageLoader imageLoader);
}
