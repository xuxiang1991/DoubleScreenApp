package com.sunmi.doublescreen.doublescreenapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.sunmi.doublescreen.doublescreenapp.R;
import com.sunmi.doublescreen.doublescreenapp.bean.ProductList;

import java.util.List;

/**
 * Created by highsixty on 2018/3/9.
 * mail  gaolulin@sunmi.com
 */

public class MenusAdapter extends BaseAdapter {

    private Context mContext;
    private List<ProductList.ProductsBean> mMenus;

    public MenusAdapter(Context context, List<ProductList.ProductsBean> menus) {
        this.mContext = context;
        this.mMenus = menus;
    }

    @Override
    public int getCount() {
        return mMenus == null ? 0 : mMenus.size();
    }

    @Override
    public Object getItem(int position) {
        return mMenus == null ? null : mMenus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold hold = null;
        if (convertView == null) {
            hold = new ViewHold();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.menus_presentation_items_layout, null);
            hold.tvId = (TextView) convertView.findViewById(R.id.tvId);
            hold.tvName = (TextView) convertView.findViewById(R.id.tvName);
            hold.tvMoney = (TextView) convertView.findViewById(R.id.tvMoney);
            hold.llyBg = (LinearLayout) convertView.findViewById(R.id.lly_bg);
            hold.tvsize = (TextView) convertView.findViewById(R.id.tvsize);
            hold.tvhot = (TextView) convertView.findViewById(R.id.tvhot);
            hold.tvcount = (TextView) convertView.findViewById(R.id.tvcount);
            convertView.setTag(hold);
        } else {
            hold = (ViewHold) convertView.getTag();
        }
        Log.d("Sunmi", "getView: ------->" + (hold.tvId == null) + "  " + (mMenus == null));
        hold.tvId.setText(mMenus.get(position).getUid().substring(mMenus.get(position).getUid().length() - 4, mMenus.get(position).getUid().length()) + "");
        hold.tvName.setText(mMenus.get(position).getName());
        hold.tvMoney.setText(mMenus.get(position).getSellPrice());
        hold.tvcount.setText("X " + mMenus.get(position).getCount());
        if ((position + 1) % 2 == 0) {
            hold.llyBg.setBackgroundColor(Color.parseColor("#F1F5F6"));
        } else {
            hold.llyBg.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        hold.tvhot.setText(getHot(mMenus.get(position).getHotType()));
        hold.tvsize.setText(getSize(mMenus.get(position).getBoxType()));
        return convertView;
    }

    class ViewHold {
        private TextView tvId;
        private TextView tvName;
        private TextView tvMoney;
        private LinearLayout llyBg;
        private TextView tvsize, tvhot, tvcount;
    }

    public void update(List<ProductList.ProductsBean> menus) {
        this.mMenus = menus;
        notifyDataSetChanged();
    }

    /**
     * 获取温度
     *
     * @param hotype
     * @return
     */
    public static String getHot(int hotype) {
        if (hotype == 0) {
            return "热";
        } else if (hotype == 1) {
            return "冷";
        } else {
            return "常温";
        }

    }

    /**
     * 获取大小
     *
     * @param boxType
     * @return
     */
    public static String getSize(int boxType) {
        if (boxType == 0) {
            return "小杯";
        } else if (boxType == 1) {
            return "中杯";
        } else {
            return "大杯";
        }

    }
}
