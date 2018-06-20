package com.sunmi.doublescreen.doublescreenapp.network.okhttp;

import android.os.Handler;
import android.os.Looper;


import com.sunmi.doublescreen.doublescreenapp.Logger;
import com.sunmi.doublescreen.doublescreenapp.network.config.Action;
import com.sunmi.doublescreen.doublescreenapp.network.config.HttpFailTip;
import com.sunmi.doublescreen.doublescreenapp.network.service.CommonRequest;
import com.sunmi.doublescreen.doublescreenapp.network.service.CommonResponse;
import com.sunmi.doublescreen.doublescreenapp.network.service.convert.Convert;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hua on 2017/1/10.
 */

public class OkhttpWrapperResponse extends CommonResponse<String> {
    private CommonResponse mCommonResponse;

    private List<Convert> mConvert;

    private Handler mDelieverHandler;

    private String action;

    /**
     * 排除有的接口不处理Token的问题
     */
    private static final List<String> actionList = new ArrayList<>();

    /**
     * 排除跳登陆的ACTION
     */
    static {
        actionList.add(Action.UPDATE_APK_ACTION);
        actionList.add(Action.UPDATE_APK_ACTION);
    }

    public OkhttpWrapperResponse(CommonResponse commonResponse, List<Convert> converts, String action) {
        this.mCommonResponse = commonResponse;
        this.mConvert = converts;
        this.mDelieverHandler = new Handler(Looper.getMainLooper());
        this.action = action;
    }

    @Override
    public void onSuccess(final CommonRequest request, final String data) {
        if (mCommonResponse == null) return;
        Logger.e("----data-----", action + "  = " + data);

        for (Convert convert : mConvert) {

            /**
             * 排除有的返回不是JSON格式  直接不处理
             */
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(data);
            } catch (Exception e) {
                //非JSON 格式处理
                mDelieverHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCommonResponse.onSuccess(request, data);
                    }
                });
                return;
            }

            try {
                if (jsonObject != null && jsonObject.has("code")) {
                    int code = jsonObject.getInt("code");

                    /**
                     * 处理TOKEN过期 给提示 跳登录
                     */
//                    if (LoginProxy.getInstance().isTokenOverdue(code)) {
////                        onFail(code, jsonObject.optString("msg"));
//                        mDelieverHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                LoginProxy.getInstance().logout();
//                                if (!actionList.contains(action)) {
//                                    if (!AppContext.getInstance().isLoginActivity) {
//                                        AppContext.getInstance().isLoginActivity = true;
//                                        Intent intent = new Intent(AppContext.getInstance(), LoginActivity.class);
//                                        intent.putExtra("isTokenOver", true);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        AppContext.getInstance().startActivity(intent);
//                                    }
//                                }
//                            }
//                        });
//                    }

                    if (getType() != String.class) {
                        try {
                            if (code >= 0) {
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
                            } else {
                                onFail(code, jsonObject.optString("msg"));
                            }
                        } catch (Exception e) {
                            onFail(HttpFailTip.JSON_ERROR, HttpFailTip.getFailTipMessage(HttpFailTip.JSON_ERROR));
                        }

                    } else {
                        //非JSON 格式处理
                        mDelieverHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mCommonResponse.onSuccess(request, data);
                            }
                        });
                    }

                } else {
                    //非JSON 格式处理
                    mDelieverHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mCommonResponse.onSuccess(request, data);
                        }
                    });
                }
            } catch (Exception e) {
                onFail(HttpFailTip.JSON_ERROR, HttpFailTip.getFailTipMessage(HttpFailTip.JSON_ERROR));
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
        if (mCommonResponse == null) return;
        mDelieverHandler.post(new Runnable() {
            @Override
            public void run() {
                mCommonResponse.onFail(errorCode, errorMsg);
            }
        });
    }

}
