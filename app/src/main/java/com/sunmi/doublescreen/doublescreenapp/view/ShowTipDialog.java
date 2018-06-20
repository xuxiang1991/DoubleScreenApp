package com.sunmi.doublescreen.doublescreenapp.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sunmi.doublescreen.doublescreenapp.R;
import com.sunmi.doublescreen.doublescreenapp.material.MeterialDialogUtil;
import com.sunmi.doublescreen.doublescreenapp.utils.Utils;

public class ShowTipDialog {

    Dialog dialog;
    Context Mcontext;
    View view;
    DialogInterface.OnDismissListener onDismissListener;

    public void setDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    // 开启加载框......
    public boolean showDialog(Context context) {
        this.Mcontext = context;

        closeDialog();

        if (!isContextValid(context)) {
            return false;
        }

        // 是否联网网络
        ConnectivityManager manger = (ConnectivityManager) Mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manger.getActiveNetworkInfo();

        // 检查当前是否有网络
        boolean isNetworkConnected = false;
        if (info != null && info.isAvailable()) {
            NetworkInfo wifiInfo = manger.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobileInfo = manger.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (null != wifiInfo && State.CONNECTED == wifiInfo.getState() || null != mobileInfo
                    && State.CONNECTED == mobileInfo.getState()) {
                isNetworkConnected = true;
            }

            if (Utils.isNetworkConnected(Mcontext)) {
                isNetworkConnected = true;
            }
        }

        if (isNetworkConnected) {
            dialog = new Dialog(Mcontext, R.style.dialog);
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.show_dialog_narmal, null);
            LinearLayout layout = (LinearLayout) v.findViewById(R.id.show_dialog);
//			ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img_loading);
//			Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.animation);
//			spaceshipImage.startAnimation(hyperspaceJumpAnimation);
            dialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnDismissListener(onDismissListener);
            dialog.show();
            return true;
        } else {

            MeterialDialogUtil.getInstance().positiveRightDialog(context, "未打开网络", "请打开您的网络连接，稍后再试!", "网络设置", "取消", new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    Mcontext.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                    dialog.dismiss();
                }
            }, new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();
                }
            });

            return false;
        }
    }

    // 关闭加载框......
    public void closeDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
        } catch (final Exception e) {
        } finally {
            dialog = null;
        }
    }

    private boolean isContextValid(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return false;
            }
        }

        return true;
    }

}
