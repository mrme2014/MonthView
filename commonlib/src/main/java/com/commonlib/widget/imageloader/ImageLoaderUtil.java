package com.commonlib.widget.imageloader;

import android.content.Context;
import android.widget.ImageView;


/*
    * Demo1:
    * ImageLoader imageLoader = new ImageLoader.Builder().url("img url").imgView(mImgView).build();
    * ImageLoaderUtil.getInstance().loadImage(context,imageLoader);
    *
    * Demo2:
    * ImageLoaderUtil.getInstance().loadImage(context, "img url", imageView)
    * */

/**
 * Created by wqf on 16/8/10.
 * use this class to load image,single instance
 */
public class ImageLoaderUtil {

    public static final int PIC_LARGE = 0;
    public static final int PIC_MEDIUM = 1;
    public static final int PIC_SMALL = 2;

    public static final int LOAD_STRATEGY_NORMAL = 0;
    public static final int LOAD_STRATEGY_ONLY_WIFI = 1;    // 只在wifi下加载

    private static ImageLoaderUtil mInstance;
    private BaseImageLoaderStrategy mStrategy;

    // 默认用Glide
    public ImageLoaderUtil() {
        mStrategy = new GlideImageLoaderStrategy();
    }

    // 全局应该只提供一个ImageLoader的实例，因为图片加载中又有线程池，缓存系统和网络请求等，很消耗资源，所以不可能让它构造多个实例。
    public static ImageLoaderUtil getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoaderUtil.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoaderUtil();
                    return mInstance;
                }
            }
        }
        return mInstance;
    }


    public void loadImage(Context context, ImageLoader img) {
        mStrategy.loadImage(context, img);
    }

    public void loadImage(Context context, String imgUrl, ImageView imageView) {
        ImageLoader imageLoader = new ImageLoader.Builder().url("img url").imageView(imageView).build();
        ImageLoaderUtil.getInstance().loadImage(context,imageLoader);
    }

    public void setLoadImgStrategy(BaseImageLoaderStrategy strategy) {
        mStrategy = strategy;
    }
}
