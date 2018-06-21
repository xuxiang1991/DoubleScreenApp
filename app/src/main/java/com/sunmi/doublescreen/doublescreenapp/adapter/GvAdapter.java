package com.sunmi.doublescreen.doublescreenapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sunmi.doublescreen.doublescreenapp.R;
import com.sunmi.doublescreen.doublescreenapp.bean.DrinkBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by highsixty on 2018/3/13.
 * mail  gaolulin@sunmi.com
 */

public class GvAdapter extends BaseAdapter {

    private Context mContext;
    private List<DrinkBean> mGvBeans;
    private int mFlag;

    public GvAdapter(Context context, List<DrinkBean> gvBeans, int flag) {
        this.mContext = context;
        this.mGvBeans = gvBeans;
        this.mFlag = flag;
    }

    @Override
    public int getCount() {
        return mGvBeans == null ? 0 : mGvBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return mGvBeans == null ? null : mGvBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold hold = null;
        if (null == convertView) {
            hold = new ViewHold();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gv_menus_layout, null);
            hold.ivPhoto = (ImageView) convertView.findViewById(R.id.iv_photo);
            hold.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            hold.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            hold.tvUnit = (TextView) convertView.findViewById(R.id.tv_unit);
            convertView.setTag(hold);
        } else {
            hold = (ViewHold) convertView.getTag();
        }
//        hold.ivPhoto.setImageResource(mGvBeans.get(position).getImgId());
        hold.tvName.setText(mGvBeans.get(position).getName());
        hold.tvPrice.setText("¥"+mGvBeans.get(position).getPrice());
//        if (mFlag == 1) {
//            if(MainActivity.isShowTime&&position==(mGvBeans.size()-1)){
//                hold.tvUnit.setText("/"+ ResourcesUtils.getString(mContext,R.string.units_each));
//            }else {
//                hold.tvUnit.setText("/"+ ResourcesUtils.getString(mContext,R.string.units_tin));
//            }
//        } else {
//            hold.tvUnit.setText("/"+ ResourcesUtils.getString(mContext,R.string.units_kg));
//        }
//        hold.ivPhoto.setImageBitmap(generateQrCodeBitmap("wwww.baidu.com",120,120));
        return convertView;
    }

    public class ViewHold {
        private ImageView ivPhoto;
        private TextView tvName;
        private TextView tvPrice;
        private TextView tvUnit;
    }





}
