package com.sunmi.doublescreen.doublescreenapp.network.service;

/**
 * Created by hua on 2017/1/10.
 */

public class CommonResponse<T> {

    /**
     * 网络请求成功回调
     * @param request
     * @param data
     */
    public void onSuccess(CommonRequest request, T data) {
        onComplete();
    }

    /**
     * 网络请求失败 或者解析数据错误
     * @param errorCode
     * @param errorMsg
     */
    public void onFail(int errorCode, String errorMsg) {
        onComplete();
    }

    /**
     * 网络请求结束统一先调用这个 可以处理关闭加载框等操作
     */
    public void onComplete() {

    }
}
