package com.sunmi.doublescreen.doublescreenapp.network.service;

import android.os.Handler;
import android.os.Looper;

import com.sunmi.doublescreen.doublescreenapp.Logger;
import com.sunmi.doublescreen.doublescreenapp.network.config.HttpFailTip;
import com.sunmi.doublescreen.doublescreenapp.network.service.convert.Convert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by hua on 2017/1/10.
 */

public class WrapperResponse extends CommonResponse<String> {
    private CommonResponse mCommonResponse;

    private List<Convert> mConvert;

    private Handler mDelieverHandler;

    public WrapperResponse(CommonResponse moocResponse, List<Convert> converts) {
        this.mCommonResponse = moocResponse;
        this.mConvert = converts;
        this.mDelieverHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onSuccess(final CommonRequest request, final String data) {
        if (mCommonResponse == null) return;
        Logger.e("----data-----", data);
        for (Convert convert : mConvert) {
            if (getType() != String.class) {
                try {
                    final Object object = convert.parse(data, getType());
                    mDelieverHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (object == null) {
                                onFail(HttpFailTip.JSON_ERROR, HttpFailTip.getFailTipMessage(HttpFailTip.JSON_ERROR));
                            } else {
                                mCommonResponse.onSuccess(request, object);
                            }
                        }
                    });
                } catch (Exception e) {
                    onFail(HttpFailTip.JSON_ERROR, HttpFailTip.getFailTipMessage(HttpFailTip.JSON_ERROR));
                }
                return;
            } else {
                //非JSON 格式处理
                mDelieverHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCommonResponse.onSuccess(request, data);
                    }
                });
            }

        }


    }


    public Type getType() {
        Type type = mCommonResponse.getClass().getGenericSuperclass();
        Type[] paramType = ((ParameterizedType) type).getActualTypeArguments();
        return paramType[0];
    }

    @Override
    public void onFail(final int errorCode, final String errorMsg) {
        Logger.e("----------", "-----fail----" + errorMsg);
        if (mCommonResponse == null) return;
        mDelieverHandler.post(new Runnable() {
            @Override
            public void run() {
                mCommonResponse.onFail(errorCode, errorMsg);
            }
        });
    }

}
