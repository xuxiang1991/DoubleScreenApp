package com.sunmi.doublescreen.doublescreenapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.DisplayMetrics;

import com.google.gson.Gson;
import com.sunmi.doublescreen.doublescreenapp.bean.ProductList;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置信息
 * Created by Dvoe on 2015/9/10.
 */
public class Config {

    //用户信息
    private static String USER_INFO = "USER_INFO";


    public static int width = 0;
    public static int height = 0;
    public static float density;
    public static int statusHeight = 0;

    public static SharedPreferences UserInfoPreferences = MyApplication.getInstance().getSharedPreferences(USER_INFO, 0);


    // 第一次启动
    private static String FIRST_START = "FIRST_START";

    public static boolean isFirst() {
        return UserInfoPreferences.getBoolean(FIRST_START, true);
    }

    public static void setFirst(boolean isFirst) {
        UserInfoPreferences.edit().putBoolean(FIRST_START, isFirst).commit();
    }


    /**
     * 得到屏幕长宽
     *
     * @param activity
     */
    public static void setScreenSize(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        density = displayMetrics.density;
        statusHeight = getStatusHeight(activity);
    }

    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }


    /**
     * 根据给定的宽和高进行拉伸
     *
     * @param origin    原图
     * @param newWidth  新图的宽
     * @param newHeight 新图的高
     * @return new Bitmap
     */
    public static Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);// 使用后乘
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }



    public static String USERINFO = "USERINFO";

    /**
     * 登陆退出
     */
    public static void ExitUser() {

        setUserInfo(null);
    }


    public static void setUserInfo(String userInfo) {
        UserInfoPreferences.edit().putString(USERINFO, userInfo).commit();
    }

//    public static User getUserInfo() {
//        String userInfo = UserInfoPreferences.getString(USERINFO, "");
//        if ("".equals(userInfo)) {
////            User Uinfo=new User();
////            Uinfo.setData(new User.DataEntity());
////            Uinfo.getData().setUserName("徐翔");
////            Uinfo.getData().setPhone("13962325335");
////            Uinfo.getData().setLevel(1);
////            Uinfo.getData().setToken("12345678");
////            return Uinfo;
//
//            return null;
//        } else {
//
//            return new Gson().fromJson(userInfo, User.class);
//        }
//
//    }


    // 当前图片资源版本号
    private static String CURRENT_LOGO = "CURRENT_LOGO";

    public static String getCurrentLOGO() {
        return UserInfoPreferences.getString(CURRENT_LOGO, "0");
    }

    public static void setCurrentLOGO(String currentLogo) {
        UserInfoPreferences.edit().putString(CURRENT_LOGO, currentLogo).commit();
    }


    /**
     * 热门产品列表
     */
    public static List<ProductList.ProductsBean> hotPorducts=new ArrayList<>();
    /**
     * 普通产品列表
     */
    public static List<ProductList.ProductsBean> comPorducts=new ArrayList<>();

}
