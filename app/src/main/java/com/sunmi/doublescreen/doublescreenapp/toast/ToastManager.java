package com.sunmi.doublescreen.doublescreenapp.toast;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;


import com.sunmi.doublescreen.doublescreenapp.MyApplication;

import java.lang.ref.WeakReference;

/**
 * 管理 toast，显示toast时先取消之前的，只有使用ToastManager显示的toast才能被取消。只能在UI线程使用这个类，否则有多线程问题
 *
 * @author Tim Yang
 */
public class ToastManager {

    private static final Context sContext = MyApplication.getInstance();
    private static WeakReference<Toast> sToast = null;

    private ToastManager() {
    }

    /**
     * 显示toast, 如果有正在显示的，先取消掉, 时间 Toast.LENGTH_SHORT
     */
    public static void show(String text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示toast, 如果有正在显示的，先取消掉， 时间 Toast.LENGTH_SHORT
     */
    public static void show(@StringRes int stringResId) {
        showToast(stringResId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示toast, 如果有正在显示的，先取消掉， 时间 Toast.LENGTH_LONG
     */
    public static void showLong(String text) {
        showToast(text, Toast.LENGTH_LONG);
    }

    /**
     * 显示toast, 如果有正在显示的，先取消掉， 时间 Toast.LENGTH_LONG
     */
    public static void showLong(@StringRes int stringResId) {
        showToast(stringResId, Toast.LENGTH_LONG);
    }

    private static void showToast(@StringRes int stringResId, int duration) {
        cancelPreviousToast();

        Toast toast = Toast.makeText(sContext, stringResId, duration);
        sToast = new WeakReference<Toast>(toast);
        toast.show();
    }

    private static void showToast(String text, int duration) {
        cancelPreviousToast();

        Toast toast = Toast.makeText(sContext, text, duration);
        sToast = new WeakReference<Toast>(toast);
        toast.show();
    }

    private static void cancelPreviousToast() {
        if (sToast != null) {
            final Toast previousToast = sToast.get();
            if (previousToast != null) {
                previousToast.cancel();
                sToast.clear();
            }
        }
    }

}
