package com.commonlib.widget.imageloader;

import android.widget.ImageView;

/**
 * Created by wqf on 16/8/10.
 * encapsulation of ImageView,Build Pattern used
 */
public class ImageLoader {
    private int type;               // 类型（大图，中图，小图）
    private String url;             // 图片url
    private int placeHolder;        // 占位图
    private ImageView imageView;    // ImageView实例
    private int wifiStrategy;       // 加载策略，是否在wifi下才加载

    private ImageLoader(Builder builder) {
        this.type = builder.type;
        this.url = builder.url;
        this.placeHolder = builder.placeHolder;
        this.imageView = builder.imageView;
        this.wifiStrategy = builder.wifiStrategy;
    }

    public int getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public int getPlaceHolder() {
        return placeHolder;
    }

    public ImageView getImgView() {
        return imageView;
    }

    public int getWifiStrategy() {
        return wifiStrategy;
    }


    public static class Builder {
        private int type;
        private String url;
        private int placeHolder;
        private ImageView imageView;
        private int wifiStrategy;

        public Builder() {
            this.type = ImageLoaderUtil.PIC_SMALL;
            this.url = "";
//            this.placeHolder = R.drawable.;
            this.imageView = null;
            this.wifiStrategy = ImageLoaderUtil.LOAD_STRATEGY_NORMAL;
        }

        public Builder type(int type) {
            this.type = type;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder placeHolder(int placeHolder) {
            this.placeHolder = placeHolder;
            return this;
        }

        public Builder imageView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public Builder strategy(int strategy) {
            this.wifiStrategy = strategy;
            return this;
        }

        public ImageLoader build() {
            return new ImageLoader(this);
        }
    }
}
