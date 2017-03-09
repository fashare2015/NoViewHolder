package com.fashare.no_view_holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by jinliangshan on 17/2/13.
 * 图片加载工具类 —— 基于 Glide
 */
public class ImageLoader {
    public static final String TAG = "ImageLoader";

    /**
     * 异步加载图片
     * @param url 图片路径
     * @param drawableId 占位图
     */
    public static void loadImage(final ImageView imageView, String url, int drawableId) {
//		DisplayImageOptions options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(drawableId)
//				.showImageForEmptyUri(drawableId)
//				.showImageOnFail(drawableId)
//				.cacheInMemory(true)
//				.cacheOnDisk(true)
//				.considerExifParams(true)
//				.build();
//		ImageLoader.getInstance().displayImage(url,this,options);

        loadImage(imageView, url, drawableId, null);
    }

    /**
     * 异步加载图片, 带回调
     * @param url 图片路径
     * @param drawableId 占位图
     * @param callback 图片加载完成回调
     */
    public static void loadImage(final ImageView imageView, String url, int drawableId, final Callback callback) {
//		DisplayImageOptions options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(drawableId)
//				.showImageForEmptyUri(drawableId)
//				.showImageOnFail(drawableId)
//				.cacheInMemory(true)
//				.cacheOnDisk(true)
//				.considerExifParams(true)
//				.build();
//		ImageLoader.getInstance().displayImage(url, this, options, new ImageLoadingListener() {
//			@Override
//			public void onLoadingStarted(String s, View view) {
//
//			}
//
//			@Override
//			public void onLoadingFailed(String s, View view, FailReason failReason) {
//
//			}
//
//			@Override
//			public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//				if (callback != null) {
//					callback.onLoadingComplete(bitmap);
//				}
//			}
//
//			@Override
//			public void onLoadingCancelled(String s, View view) {
//
//			}
//		});
//        Log.d("CXB_", "开始下载:" + System.currentTimeMillis());

        if(imageView == null) {
            Log.e(TAG, "loadImage() -> imageView is null");
            return;
        }

        Context context = imageView.getContext();
        Glide.with(context)
                .load(url)
                .asBitmap()
                .placeholder(drawableId)
                .error(drawableId)
                .into(new BitmapImageViewTarget(imageView){
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        super.onResourceReady(bitmap, glideAnimation);
                        if (callback != null) {
                            callback.onLoadingComplete(bitmap);
                        }
                    }
                });
    }

    /**
     * 异步加载圆角图片, 使用 Glide 内置的圆角裁剪 —— bitmapTransform.
     *
     * 注意: bitmapTransform 与 外在的裁剪不兼容。即: https://github.com/wasabeef/glide-transformations/issues/54
     *
     *      1. 此时的 imageView 不能是自定义的圆角View.
     *      2. scaleType 也不能设。
     *
     * 否则重复裁剪, 会有 bug.
     *
     * @param url 图片路径
     * @param drawableId 占位图
     * @param radiusPixels 圆角像素值
     */
    public static void loadRoundImage(ImageView imageView, String url, int drawableId, int radiusPixels) {
//		DisplayImageOptions options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(drawableId)
//				.showImageForEmptyUri(drawableId)
//				.showImageOnFail(drawableId)
//				.cacheInMemory(true)
//				.cacheOnDisk(true)
//				.considerExifParams(true)
//				.displayer(new RoundedBitmapDisplayer(radiusPixels))
//				.build();
//		ImageLoader.getInstance().displayImage(url,this,options);

        if(imageView == null) {
            Log.e(TAG, "loadRoundImage() -> imageView is null");
            return;
        }

        Context context = imageView.getContext();
        Glide.with(context)
                .load(url)
                .placeholder(drawableId)
                .error(drawableId)
                .bitmapTransform(
                        new CenterCrop(context),
                        new RoundedCornersTransformation(context, radiusPixels, 0)
                )
                .crossFade()
                .into(imageView);
    }

    /**
     * 异步加载图片, 带回调(不涉及 ImageView)
     * @param url 图片路径
     * @param callback 图片加载完成回调
     */
    public static void loadImage(Context context, String url, final Callback callback) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(){
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (callback != null) {
                            callback.onLoadingComplete(bitmap);
                        }
                    }
                });
    }

    public interface Callback {
        void onLoadingComplete(Bitmap bitmap);
    }
}
