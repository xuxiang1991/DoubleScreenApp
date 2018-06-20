package com.sunmi.doublescreen.doublescreenapp.img;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.sunmi.doublescreen.doublescreenapp.MyApplication;
import com.sunmi.doublescreen.doublescreenapp.R;

/**
 * 图片加载
 * Created by Administrator on 2015/5/6.
 */
public class LoadingImgUtil {

    //博客详解
    //http://blog.csdn.net/shangmingchao/article/details/51125554/


    public static void loadLiveScoreImg(String url, ImageView imgview) {
        Glide.with(MyApplication.getInstance()).load(url)
//                .transform(new GlideRoundTransform(MyApplication.getInstance()))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.drawable.wusaishi)
                .error(R.drawable.wusaishi)
                .into(imgview);
//      Glide.with(this).load(url).bitmapTransform(new CropCircleTransformation(this)).crossFade(1000).into(image2);

    }

    public static void loadNoBgImg(String url, ImageView imageView) {
        Glide.with(MyApplication.getInstance()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }

    public static void loadNoBgImgByListener(String url, ImageView imageView, RequestListener<String, GlideDrawable> listener) {
        Glide.with(MyApplication.getInstance()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT).listener(listener).into(imageView);
    }

    public static void loadNewsImg(String url, ImageView imgview) {
        Glide.with(MyApplication.getInstance()).load(url)
                .placeholder(R.drawable.jdd_default)
                .error(R.drawable.jdd_default)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imgview);
    }

    public static void loadPostImg(String url, ImageView imgview) {
        Glide.with(MyApplication.getInstance()).load(url)
                .placeholder(R.drawable.jdd_post_default)
                .error(R.drawable.jdd_post_default)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imgview);
    }

    public static void loadADImg(String url, ImageView imgview) {
        Glide.with(MyApplication.getInstance()).load(url)
//                .placeholder(R.drawable.ad_tmp)
                .error(R.drawable.ad_tmp)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imgview);

    }

    public static void loadRounedImg(String url, ImageView imgview) {
        Glide.with(MyApplication.getInstance()).load(url).
                transform(new GlideRoundTransform(MyApplication.getInstance()))
                .placeholder(R.drawable.mr)
                .error(R.drawable.mr)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imgview);
    }

    public static void loadBannerImg(String url, ImageView imgview) {
//        RotateAnimation mAnim = new RotateAnimation(0,360, Animation.RESTART,0.5f,Animation.RESTART,0.5f);
//        mAnim.setDuration(1500);
//        mAnim.setRepeatCount(Animation.INFINITE);
//        mAnim.setRepeatMode(Animation.RESTART);
//        mAnim.setStartTime(Animation.START_ON_FIRST_FRAME);

        Glide.with(MyApplication.getInstance()).load(url)
//                transform(new GlideRoundTransform(MyApplication.getInstance()))
//                .animate(mAnim)
//                .skipMemoryCache(true)
                .placeholder(R.drawable.ad_tmp)
                .error(R.drawable.ad_tmp)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imgview);
    }


    public static void loadImage(String url, ImageView imgview, RequestListener listener) {
        Glide.with(MyApplication.getInstance()).load(url).
                transform(new GlideRoundTransform(MyApplication.getInstance()))
                .listener(listener)
                .placeholder(R.drawable.wusaishi)
                .error(R.drawable.wusaishi)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imgview);

    }

    public static void loadInfoImg(String url, ImageView imgview) {
        Glide.with(MyApplication.getInstance()).load(url)
                .placeholder(R.drawable.img_list_pic)
                .error(R.drawable.img_list_pic)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imgview);
    }

    /**
     * 加载欢迎页
     *
     * @param url
     * @param imgview
     */
    public static void loadStartupImage(String url, ImageView imgview) {
        Glide.with(MyApplication.getInstance()).load(url)
                .placeholder(R.drawable.startup)
                .error(R.drawable.startup)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imgview);

    }
}
