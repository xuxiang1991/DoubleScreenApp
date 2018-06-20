///**
// *
// */
//package com.sunmi.doublescreen.doublescreenapp.activity;
//
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//
//import com.sunmi.doublescreen.doublescreenapp.ActivityManager;
//import com.sunmi.doublescreen.doublescreenapp.Config;
//import com.sunmi.doublescreen.doublescreenapp.toast.ToastManager;
//
//
///**
// * @author zhangwei
// */
//public abstract class BaseToolbarActivity extends AppCompatActivity {
//    protected static final String TAG = BaseToolbarActivity.class.getSimpleName();
//    protected BaseToolbarActivity self;
//    protected Toolbar toolbar;
//    protected ActivityManager activityManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        self = this;
//        activityManager = ActivityManager.getScreenManager();
//        activityManager.pushActivity(this);
//        int layoutId = getLayoutId();
//        if (layoutId != 0) {
//            setContentView(layoutId);
//            // 删除窗口背景
//            getWindow().setBackgroundDrawable(null);
//        }
//        preliminary();
//
//        // 设置分享的内容
//
//
//        if (Config.width * Config.height == 0)
//            Config.setScreenSize(this);//获取屏幕宽高
//
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        activityManager.popActivity(this);
//        super.onDestroy();
//    }
//
//
//    /**
//     * 向用户展示信息前的准备工作在这个方法里处理
//     */
//    protected void preliminary() {
//        // 初始化组件
//        setupViews();
//
//        // 初始化数据
//        initialized();
//    }
//
//
//    /**
//     * 布局文件ID
//     *
//     * @return
//     */
//    protected abstract int getLayoutId();
//
//
//    /**
//     * 初始化组件
//     */
//    protected abstract void setupViews();
//
//    /**
//     * 初始化数据
//     */
//    protected abstract void initialized();
//
//    /**
//     * Debug输出Log信息
//     *
//     * @param msg
//     */
//    protected void debugLog(String msg) {
//        Log.d(TAG, msg);
//    }
//
//    /**
//     * Error输出Log信息
//     *
//     * @param msg
//     */
//    protected void errorLog(String msg) {
//        Log.e(TAG, msg);
//    }
//
//    /**
//     * Info输出Log信息
//     *
//     * @param msg
//     */
//    protected void showLog(String msg) {
//        Log.i(TAG, msg);
//    }
//
//
////    // TitleBar标题栏
////    public void titleBar(String titleName) {
////        toolbar = (Toolbar) findViewById(R.id.toolbar);
////        toolbar.setTitle(titleName);
////        toolbar.setNavigationIcon(R.drawable.top_title_back_bg_normal);
////
////        setSupportActionBar(toolbar);
////
//////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
////        getSupportActionBar().setHomeButtonEnabled(true);
////    }
////
////    // TitleBar标题栏
////    public void titleBar(int resTitle) {
////        toolbar = (Toolbar) findViewById(R.id.toolbar);
////        toolbar.setTitle(resTitle);
////        toolbar.setNavigationIcon(R.drawable.top_title_back_bg_normal);
////
////        setSupportActionBar(toolbar);
////
//////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
////        getSupportActionBar().setHomeButtonEnabled(true);
////    }
//
//    /**
//     * 长时间显示Toast提示(来自String)
//     *
//     * @param message
//     */
//    protected void ShowToast(String message) {
//        ToastManager.showLong(message);
//    }
//
//    /**
//     * 长时间显示Toast提示(来自res)
//     *
//     * @param resId
//     */
//    protected void ShowToast(int resId) {
//        ToastManager.showLong(resId);
//    }
//
//    /**
//     * 短暂显示Toast提示(来自res)
//     *
//     * @param resId
//     */
//    protected void ShowShortToast(int resId) {
//        ToastManager.show(resId);
//    }
//
//    /**
//     * 短暂显示Toast提示(来自String)
//     *
//     * @param text
//     */
//    protected void ShowShortToast(String text) {
//        ToastManager.show(text);
//    }
//}
