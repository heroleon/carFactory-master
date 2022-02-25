package com.leon.carfixfactory.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.leon.carfixfactory.R;


/**
 *   DiskCacheStrategy.ALL 使用DATA和RESOURCE缓存远程数据，仅使用RESOURCE来缓存本地数据。
     DiskCacheStrategy.NONE 不使用磁盘缓存
     DiskCacheStrategy.DATA 在资源解码前就将原始数据写入磁盘缓存
     DiskCacheStrategy.RESOURCE 在资源解码后将数据写入磁盘缓存，即经过缩放等转换后的图片资源。
     DiskCacheStrategy.AUTOMATIC 根据原始图片数据和资源编码策略来自动选择磁盘缓存策略。
 */
public class GlideUtils {
    public static final int duration = 350;

    public static final int placeholder = R.mipmap.icon_default_avatar;
    public static final int error = R.mipmap.icon_default_avatar;
    public static final int fallback =  R.mipmap.icon_default_avatar;

    public static final int circlePlaceholder = R.mipmap.icon_default_avatar;
    public static final int circleError =  R.mipmap.icon_default_avatar;
    public static final int circleFallback =  R.mipmap.icon_default_avatar;

    public static RequestOptions centerCropRequestOptions;
    public static RequestOptions centerCropCircleRequestOptions;
    public static RequestOptions defaultRequestOptions;
    public static RequestOptions defaultCircleRequestOptions;

    public static TransitionOptions transitionOptions;

    static {
        if (defaultRequestOptions == null) {
            defaultRequestOptions = new RequestOptions()
                    //                    .placeholder(placeholder)
                    .error(error)
                    .fallback(fallback)
                    .priority(Priority.HIGH)
                    .format(DecodeFormat.PREFER_RGB_565)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        }

        if (centerCropRequestOptions == null) {
            centerCropRequestOptions = new RequestOptions()
                    //                    .placeholder(placeholder)
                    .error(error)
                    .fallback(fallback)
                    .priority(Priority.HIGH)
                    .format(DecodeFormat.PREFER_RGB_565)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        }


        if (defaultCircleRequestOptions == null) {
            defaultCircleRequestOptions = new RequestOptions()
                    //                    .placeholder(circlePlaceholder)
                    .error(circleError)
                    .fallback(circleFallback)
                    .priority(Priority.HIGH)
                    .format(DecodeFormat.PREFER_RGB_565)
                    .circleCrop() // .transform(new CircleCrop());
                    .diskCacheStrategy(DiskCacheStrategy.ALL);


        }

        if (centerCropCircleRequestOptions == null) {
            centerCropCircleRequestOptions = new RequestOptions()
                    //                    .placeholder(circlePlaceholder)
                    .error(circleError)
                    .fallback(circleFallback)
                    .priority(Priority.HIGH)
                    .format(DecodeFormat.PREFER_RGB_565)
                    .centerCrop()
                    .circleCrop() // .transform(new CircleCrop());
                    .diskCacheStrategy(DiskCacheStrategy.ALL);


        }

        if (transitionOptions == null) {
            transitionOptions = new DrawableTransitionOptions().crossFade(duration);
        }
    }

    public static void loadDefaultImage(Context context, Object path, ImageView iv) {
        Glide.with(context)
                .load(path)
                .apply(defaultRequestOptions)
                .transition(transitionOptions)
                .into(iv);
    }

    public static void loadCenterCropImage(Context context, Object path, ImageView iv) {
        Glide.with(context)
                .load(path)
                .apply(centerCropRequestOptions)
                .transition(transitionOptions)
                .into(iv);
    }

    public static void loadDefaultImage(Context context, Object path, ImageView iv,RequestOptions options) {
        Glide.with(context)
                .load(path)
                .apply(options)
                .transition(transitionOptions)
                .into(iv);
    }


    public static void loadDefaultCircleImage(Context context, Object path, ImageView iv) {
        //AutoSizeCompat.cancelAdapt(context.getResources());

        Glide.with(context)
                .load(path)
                .apply(defaultCircleRequestOptions)
                .transition(transitionOptions)
                .into(iv);

    }

    public static void loadCenterCropCircleImage(Context context, Object path, ImageView iv) {
      //  AutoSizeCompat.cancelAdapt(context.getResources());

        Glide.with(context)
                .load(path)
                .apply(centerCropCircleRequestOptions)
                .transition(transitionOptions)
                .into(iv);

    }

    public static void loadDefaultCircleImage(Context context, Object path, ImageView iv,RequestOptions options) {
        //AutoSizeCompat.cancelAdapt(context.getResources());

        Glide.with(context)
                .load(path)
                .apply(options)
                .transition(transitionOptions)
                .into(iv);

    }

    public interface GlideLoadBitmapCallback{
        void getBitmap(Bitmap resource);
    }
}
