package com.sunmi.doublescreen.doublescreenapp.network.config;

/**
 * Created by hua on 2017/1/11.
 */

public class HttpFailTip {
    public static final int NETWORK_ERROR = -1;
    public static final int JSON_ERROR = -2;
    public static final int UPLOAD_ERROR = -3;
    public static final int DOWNLOAD_ERROR = -4;
    public static final int OTHER_ERROR = -5;


    public static String getFailTipMessage(int code) {
        String message = "";
        switch (code) {
            case NETWORK_ERROR:
                message = "网络错误";
                break;
            case JSON_ERROR:
                message = "解析数据错误";
                break;
            case UPLOAD_ERROR:
                message = "上传失败,请重试";
                break;
            case DOWNLOAD_ERROR:
                message = "下载失败,请重试";
                break;
            default:
                message = "未知错误";

        }
        return message;
    }
}
