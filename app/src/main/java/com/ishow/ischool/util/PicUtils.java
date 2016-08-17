package com.ishow.ischool.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


/**
 * Created by abel on 15/11/5.
 */
public class PicUtils {

    public static void loadAvatarRoundRect(Context context, ImageView imageView, String imageUrl) {
        if (context == null) {
            return;
        }

        if (imageUrl != null && !"".equals(imageUrl)) {
            Glide.with(context)
                    .load(imageUrl)
                    .dontAnimate()
//                    .placeholder(R.drawable.ic_default_avatar_round)
                    .into(imageView);
        } else {
//            imageView.setImageResource(R.drawable.ic_default_avatar_round);
        }
    }

    public static void loadAvatarCircle(Context context, ImageView imageView, String imageUrl) {
        if (context == null) {
            return;
        }

        if (imageUrl != null && !"".equals(imageUrl)) {
            Glide.with(context)
                    .load(imageUrl)
                    .dontAnimate()
//                    .placeholder(R.drawable.ic_default_avatar_round)
                    .into(imageView);
        } else {
//            imageView.setImageResource(R.drawable.ic_default_avatar_round);
        }
    }

    public static void loadAvatarRect(Context context, ImageView imageView, String imageUrl) {
        if (context == null) {
            return;
        }

        if (imageUrl != null && !"".equals(imageUrl)) {
            Glide.with(context)
                    .load(imageUrl)
                    .dontAnimate()
//                    .placeholder(R.drawable.ic_default_avatar_square)
                    .into(imageView);
        } else {
            //imageView.setImageResource(R.drawable.ic_default_avatar_square);
        }
    }

    public static void loadpic(Context context, ImageView imageView, String imageUrl) {
        if (context == null) {
            return;
        }

        if (imageUrl != null && !"".equals(imageUrl)) {
            Glide.with(context)
                    .load(imageUrl)
                    .into(imageView);
        }
    }

    public static void loadpicBig(Context context, ImageView imageView, String imageUrl) {
        if (context == null) {
            return;
        }

        if (imageUrl != null && !"".equals(imageUrl)) {
            Glide.with(context)
                    .load(imageUrl)
                    .dontAnimate()
                    //.placeholder(R.drawable.ic_loading_big)
                    .into(imageView);
        } else {
            //imageView.setImageResource(R.drawable.ic_loading_big);
        }
    }

    public static void loadpicSmall(Context context, ImageView imageView, String imageUrl) {
        if (context == null) {
            return;
        }

        if (imageUrl != null && !"".equals(imageUrl)) {
            Glide.with(context)
                    .load(imageUrl)
                    .dontAnimate()
                    //.placeholder(R.drawable.ic_loading_small)
                    .into(imageView);
        } else {
            //imageView.setImageResource(R.drawable.ic_loading_small);
        }
    }
}
