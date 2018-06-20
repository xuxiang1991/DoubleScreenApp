package com.sunmi.doublescreen.doublescreenapp.material;

import android.content.Context;
import android.text.Html;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sunmi.doublescreen.doublescreenapp.R;


/**
 * Created by hua on 2017/3/20.
 */

public class MeterialDialogUtil {

    private static MeterialDialogUtil meterialDialogUtil;


    private MeterialDialogUtil() {

    }

    public static MeterialDialogUtil getInstance() {
        if (meterialDialogUtil == null) {
            meterialDialogUtil = new MeterialDialogUtil();
        }
        return meterialDialogUtil;
    }


    /**
     * 有标题 positiveText negativeText 同一级别 颜色为 #557bcc
     *
     * @param context
     */
    public void sameBasicDialog(Context context, String title, String content, String positive, String negative, MaterialDialog.SingleButtonCallback onPositive
            , MaterialDialog.SingleButtonCallback onNegative) {
        new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(positive)
                .negativeText(negative)
                .titleColorRes(R.color.text_color_selected)
                .contentColorRes(R.color.dark_gray)
                .positiveColorRes(R.color.dialog_line_bottom)
                .negativeColorRes(R.color.dialog_line_bottom)
                .backgroundColorRes(R.color.white)
                .onPositive(onPositive)
                .onNegative(onNegative)
                .show();
    }


    /**
     * 有标题 positiveText negativeText 同一级别 颜色为 #557bcc
     * 福彩3D 排3用
     *
     * @param context
     */
    public void sameBasicDialogWithDissmiss(Context context, String title, String content, String positive, String negative, MaterialDialog.SingleButtonCallback onPositive
            , MaterialDialog.SingleButtonCallback onNegative, MaterialDialog.OnDismissListener onDissmissListener) {
        new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(positive)
                .negativeText(negative)
                .titleColorRes(R.color.text_color_selected)
                .contentColorRes(R.color.dark_gray)
                .positiveColorRes(R.color.dialog_line_bottom)
                .negativeColorRes(R.color.dialog_line_bottom)
                .backgroundColorRes(R.color.white)
                .onPositive(onPositive)
                .onNegative(onNegative)
                .dismissListener(onDissmissListener)
                .show();
    }


    /**
     * 有标题 positiveText 在右边  颜色为蓝 #557bcc  左边的按钮灰色
     *
     * @param context
     */
    public void positiveRightDialog(Context context, String title, String content, String positive, String negative, MaterialDialog.SingleButtonCallback onPositive
            , MaterialDialog.SingleButtonCallback onNegative) {
        new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(positive)
                .negativeText(negative)
                .titleColorRes(R.color.text_color_selected)
                .contentColorRes(R.color.dark_gray)
                .positiveColorRes(R.color.dialog_line_bottom)
                .negativeColorRes(R.color.dark_gray)
                .backgroundColorRes(R.color.white)
                .onPositive(onPositive)
                .onNegative(onNegative)
                .show();
    }


    /**
     * 支付密码弹窗
     *
     * @param context
     * @param inputCallback
     */
    public void PaybyPassDialog(Context context, MaterialDialog.InputCallback inputCallback, MaterialDialog.SingleButtonCallback onNegative) {
        new MaterialDialog.Builder(context)
                .title("为了账户和资金安全")
                .backgroundColorRes(R.color.white)
                .titleColorRes(R.color.text_color_selected)
                .positiveColorRes(R.color.dialog_line_bottom)
                .negativeColorRes(R.color.dialog_button)
                .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .contentColorRes(R.color.text_color_selected)
                .inputRange(6, 15)
                .canceledOnTouchOutside(false)
                .positiveText("确定")
                .negativeText("取消")
                .onNegative(onNegative)
                .autoDismiss(false)
                .input("请输入支付密码", "", false, inputCallback)
                .show();
    }

    /**
     * 提现弹窗
     *
     * @param context
     * @param inputCallback
     */
    public void getMoneybyPassDialog(Context context, MaterialDialog.InputCallback inputCallback, MaterialDialog.SingleButtonCallback onNegative, MaterialDialog.SingleButtonCallback onNeutral) {
        new MaterialDialog.Builder(context)
                .title("请输入支付密码")
                .backgroundColorRes(R.color.white)
                .titleColorRes(R.color.text_color_selected)
                .positiveColorRes(R.color.dialog_line_bottom)
                .neutralColorRes(R.color.dialog_button)
                .negativeColorRes(R.color.dialog_button)
                .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .widgetColorRes(R.color.dialog_line_bottom)
                .contentColorRes(R.color.text_color_selected)
                .inputRange(6, 15)
                .canceledOnTouchOutside(false)
                .positiveText("确定")
                .negativeText("取消")
                .neutralText("忘记密码")
                .onNeutral(onNeutral)
                .onNegative(onNegative)
                .autoDismiss(false)
                .input("请输入支付密码", "", false, inputCallback)
                .show();
    }

    /**
     * 支付密码
     *
     * @param context
     * @param onPositive
     */
    public void PaybyPassOpenDialog(Context context, MaterialDialog.SingleButtonCallback onPositive, MaterialDialog.SingleButtonCallback onNegative) {
        new MaterialDialog.Builder(context)
                .title("提示")
                .backgroundColorRes(R.color.white)
                .titleColorRes(R.color.text_color_selected)
                .positiveColorRes(R.color.dialog_button)
                .negativeColorRes(R.color.dialog_line_bottom)
                .contentColorRes(R.color.dialog_info)
                .content("为保证您的资金安全，建议您在支付时启用支付密码")
                .positiveText("立即开启")
                .canceledOnTouchOutside(false)
                .negativeText("以后再说")
                .onPositive(onPositive)
                .onNegative(onNegative)
                .show();
    }


    /**
     * 普通提示框
     *
     * @param context
     * @param onPositive
     */
    public void CommonPromptDialog(Context context, String title, String content, String positiveText, MaterialDialog.SingleButtonCallback onPositive) {
        new MaterialDialog.Builder(context)
                .title(title)
                .backgroundColorRes(R.color.white)
                .titleColorRes(R.color.text_color_selected)
                .positiveColorRes(R.color.dialog_line_bottom)//common_red_main红
                .contentColorRes(R.color.dialog_info)
                .content(content)
                .positiveText(positiveText)
                .canceledOnTouchOutside(true)
                .onPositive(onPositive)
                .autoDismiss(true)
                .show();
    }


    public void showShowCancelDismissCallbacks(Context context, String title, String content, SpannableStringBuilder builderContent, String htmlContent, String positiveText, String negativeText, String neutralText, MaterialDialog.SingleButtonCallback onPositive, MaterialDialog.SingleButtonCallback onNegative, MaterialDialog.SingleButtonCallback onNetual, MaterialDialog.OnCancelListener onCancelListener, int icon) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.backgroundColorRes(R.color.white);
        if (icon > 0) {
            builder.iconRes(icon)
                    .limitIconToDefaultSize();
        }
        builder.title(title)
                .titleColorRes(R.color.text_color_selected);
        if (!TextUtils.isEmpty(content)) {
            builder.contentColorRes(R.color.dialog_info)
                    .content(content);
        } else if (!TextUtils.isEmpty(builderContent)) {
            builder.contentColorRes(R.color.dialog_info)
                    .content(builderContent);
        } else if (!TextUtils.isEmpty(htmlContent)) {
            builder.contentColorRes(R.color.dialog_info)
                    .content(Html.fromHtml(htmlContent));
        }

        if (!TextUtils.isEmpty(positiveText)) {
            builder.positiveColorRes(R.color.dialog_line_bottom)//common_red_main红
                    .positiveText(positiveText)
                    .onPositive(onPositive);
        }
        if (!TextUtils.isEmpty(negativeText)) {
            builder.negativeText(negativeText)
                    .negativeColorRes(R.color.dialog_line_bottom)
                    .onNegative(onNegative);
        }
        if (!TextUtils.isEmpty(neutralText)) {
            builder.neutralText(neutralText)
                    .neutralColorRes(R.color.dialog_line_bottom)
                    .onNeutral(onNetual);
        }
        if (onCancelListener != null) {
            builder.cancelListener(onCancelListener);
        }
        builder.canceledOnTouchOutside(false);
        builder.autoDismiss(false);
        builder.show();
    }




    /**
     * 登录密码错误弹窗
     *
     * @param context
     */
    public void LoginTipDialog(Context context, String content, MaterialDialog.SingleButtonCallback onPositive, MaterialDialog.SingleButtonCallback onNegative) {
        new MaterialDialog.Builder(context)
                .title("温馨提示")
                .backgroundColorRes(R.color.white)
                .titleColorRes(R.color.text_color_selected)
                .positiveColorRes(R.color.dialog_line_bottom)
                .negativeColorRes(R.color.dialog_line_bottom)
                .contentColorRes(R.color.dialog_info)
                .content(content)
                .positiveText("短信登录")
                .onPositive(onPositive)
                .negativeText("找回密码")
                .onNegative(onNegative)
                .canceledOnTouchOutside(true)
                .autoDismiss(true)
                .show();
    }





    /**
     * 11选5选号不足一注弹框
     *
     * @param context
     */
    public void NoOneTipDialog(Context context, String content, MaterialDialog.SingleButtonCallback onPositive, MaterialDialog.SingleButtonCallback onNegative) {
        new MaterialDialog.Builder(context)
                .title("温馨提示")
                .backgroundColorRes(R.color.white)
                .titleColorRes(R.color.text_color_selected)
                .positiveColorRes(R.color.dialog_line_bottom)
                .negativeColorRes(R.color.dialog_line_bottom)
                .contentColorRes(R.color.dialog_info)
                .content(content)
                .positiveText("确定")
                .onPositive(onPositive)
                .negativeText("取消")
                .onNegative(onNegative)
                .canceledOnTouchOutside(true)
                .autoDismiss(true)
                .show();
    }


}
