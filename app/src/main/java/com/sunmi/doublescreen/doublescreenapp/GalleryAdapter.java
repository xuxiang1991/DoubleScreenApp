package com.sunmi.doublescreen.doublescreenapp;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称：DSD
 * 类描述：
 * 创建人：Abtswiath丶lxy
 * 创建时间：2016/10/10 15:18
 * 修改人：longx
 * 修改时间：2016/10/10 15:18
 * 修改备注：
 */
public class GalleryAdapter extends PagerAdapter {

    private List<String> mPaths;
    private Context mContext;
    private List<ImageView> mViews = new ArrayList<>();

    public GalleryAdapter(List<String> paths, Context context, int screenWidth, int screenHeight) {
        this.mPaths = paths;
        this.mContext = context;
        for (int i = 0; i < 5; i++) {
            ImageView imageview = new ImageView(mContext);
            imageview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
            mViews.add(imageview);
        }
    }

    @Override
    public int getCount() {
        return mPaths.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        ImageView imageview = mViews.get(position);
//        imageview.setImageBitmap(null);
        container.removeView(container);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final ImageView imageview = mViews.get(position % 5);
        Glide.with(mContext).load(new File(mPaths.get(position))).into(imageview);
        if (imageview.getParent() != null) {
            ((ViewGroup) container).removeView(imageview);
        }
        container.addView(imageview);
        return imageview;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
