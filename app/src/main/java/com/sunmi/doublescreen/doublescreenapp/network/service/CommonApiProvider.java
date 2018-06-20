package com.sunmi.doublescreen.doublescreenapp.network.service;

import android.text.TextUtils;

import com.sunmi.doublescreen.doublescreenapp.Logger;
import com.sunmi.doublescreen.doublescreenapp.MyApplication;
import com.sunmi.doublescreen.doublescreenapp.network.config.HttpFailTip;
import com.sunmi.doublescreen.doublescreenapp.network.okhttp.OkhttpWrapperResponse;
import com.sunmi.doublescreen.doublescreenapp.network.okhttp.download.DownloadCallback;
import com.sunmi.doublescreen.doublescreenapp.network.service.convert.Convert;
import com.sunmi.doublescreen.doublescreenapp.network.service.convert.JsonConvert;
import com.sunmi.doublescreen.doublescreenapp.network.utills.HttpsUtils;
import com.sunmi.doublescreen.doublescreenapp.utils.MD5Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by hua on 2017/1/10.
 */

public class CommonApiProvider {
    private static final String ENCODING = "utf-8";
    private static final String EncryptionType = "AES";
    private static final String KEY = "d3YmI1BUOSE2S2YmalBVZUQ=";

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");//mdiatype 这个需要和服务端保持一致

    private static final List<Convert> sConverts = new ArrayList<>();
    private static OkHttpClient okHttpClient;

    /**
     * 排除有的接口不处理Token的问题
     */
    private static final List<String> actionList = new ArrayList<>();


    static {
        sConverts.add(new JsonConvert());
        /**
         * 添加时时刷新的接口
         */

        /**
         * 暂时先将GET请求 复用
         */
        File cacheFile = new File(MyApplication.getInstance().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb

        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        okHttpBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        okHttpBuilder.followRedirects(true);//允许被重定向
        okHttpBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        okHttpBuilder.cache(cache);

        okHttpClient = okHttpBuilder.build();
    }

    public static String Uuid = "3C075461B9B0";
    private static String mKey = "8888";

    public static String getKey() {
        return mKey;
    }

    public static void setUUid(String uuid) {
        if (TextUtils.isEmpty(uuid))
            Uuid = "3C075461B9B0";
        else
            Uuid = uuid;
    }


//    /**
//     * 获取公共参数头文件
//     *
//     * @param action
//     * @return
//     */
//    private static JSONObject getHeadParam(String action) {
//        JSONObject map = new JSONObject();
//        try {
//            map.put("platformVersion", android.os.Build.VERSION.RELEASE);
//            map.put("platformCode", "Android");
//            map.put("cmdID", ApplicationData.getInstance().getCommenderID() + "");
//            map.put("cmdName", ApplicationData.getInstance().getCommenderName());
//            map.put("token", UserConfiger.read(MyApplication.getInstance()).userToken);
//            map.put("appVersion", ApplicationData.APPVERSION);
//            map.put("userID", ApplicationData.getInstance().getUserID());
//            map.put("userType", TextUtils.isEmpty(ApplicationData.getInstance().getUserType()) ? 1 : ApplicationData.getInstance().getUserType());
//            map.put("uuid", Uuid);
//            map.put("action", action);
//            map.put("phoneName", android.os.Build.BRAND);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Logger.e("getHeadParam", map.toString());
//        return map;
//    }


    public static String getparamArr(String paramArr, String action) {
        JSONObject jsonObject = new JSONObject();
        try {
//            jsonObject.put("header", getHeadParam(action));
            jsonObject.put("body", paramArr);
            Logger.e("getBodyParam", action + "----" + paramArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


    /**
     * 通用POST 请求
     * 通过表单方式上传
     *
     * @param url      域名
     * @param paramArr 请求参数
     * @param action   接口 ID
     * @param response 回调
     */
    public static void getNetPostCommon(final String url, final String action, final String paramArr, CommonResponse response) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        //设置类型
        builder.setType(MultipartBody.FORM);

        try {
//            String ency = Aes.encryptContent(getparamArr(paramArr, action), EncryptionType, KEY, ENCODING);
//            builder.addFormDataPart("request", ency);

            builder.addFormDataPart("request",paramArr);
        } catch (Exception e) {
            e.printStackTrace();
        }


        RequestBody body = builder.build();

        final OkhttpWrapperResponse wrapperResponse = new OkhttpWrapperResponse(response, sConverts, action);





        OkHttpClient client = new OkHttpClient();

        Call call;
        final Request request = new Request.Builder().
                url(url).post(body).build();

        if ("https".equals(URI.create(url).getScheme().toLowerCase())) {
            //参数为NULL设置可访问所有的https网站 ，这里可以支持自签名证书
            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
            call = client.newBuilder().
                    sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager).hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            }).build().newCall(request);
        } else {
            call = client.newBuilder().build().newCall(request);
        }
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                    wrapperResponse.onFail(HttpFailTip.NETWORK_ERROR, HttpFailTip.getFailTipMessage(HttpFailTip.NETWORK_ERROR));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String string = response.body().string();

                    if (TextUtils.isEmpty(string)) {
                        wrapperResponse.onFail(HttpFailTip.NETWORK_ERROR, HttpFailTip.getFailTipMessage(HttpFailTip.NETWORK_ERROR));
                    } else {
                        wrapperResponse.onSuccess(null, string);
                    }

                } else {
                    wrapperResponse.onFail(HttpFailTip.NETWORK_ERROR, HttpFailTip.getFailTipMessage(HttpFailTip.NETWORK_ERROR));
                }
            }
        });
    }


    /**
     * 通用POST 请求  无公共参数
     *
     * @param url      域名
     * @param paramArr 请求参数
     * @param response 回调
     */

    public static void getPostCommon(String url, Map<String, String> paramArr, CommonResponse response) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        StringBuilder tempParams = new StringBuilder();
        if (paramArr != null) {
            int pos = 0;
            for (String key : paramArr.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                try {
                    tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramArr.get(key), "utf-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                pos++;
            }
        }
        //生成参数
        String params = tempParams.toString();
        //创建一个请求实体对象 RequestBody
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, params);

        final WrapperResponse wrapperResponse = new WrapperResponse(response, sConverts);
        Request request = new Request.Builder().
                url(url).post(body).build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                wrapperResponse.onFail(HttpFailTip.NETWORK_ERROR, HttpFailTip.getFailTipMessage(HttpFailTip.NETWORK_ERROR));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    try {
                        String string = response.body().string();

                        if (TextUtils.isEmpty(string)) {
                            wrapperResponse.onFail(HttpFailTip.NETWORK_ERROR, HttpFailTip.getFailTipMessage(HttpFailTip.NETWORK_ERROR));
                        } else {
                            wrapperResponse.onSuccess(null, string);
                        }
                    } catch (Exception e) {
                        wrapperResponse.onFail(HttpFailTip.NETWORK_ERROR, HttpFailTip.getFailTipMessage(HttpFailTip.NETWORK_ERROR));
                    }
                } else {
                    wrapperResponse.onFail(HttpFailTip.NETWORK_ERROR, HttpFailTip.getFailTipMessage(HttpFailTip.NETWORK_ERROR));
                }
            }
        });

    }

    /**
     * 通用的GET请求，不带参数
     *
     * @param url
     * @param response
     */
    public static void getNetGetCommon(String url, CommonResponse response) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        Logger.e("-------getURL--", url);
        final WrapperResponse wrapperResponse = new WrapperResponse(response, sConverts);
        Request request = new Request.Builder().
                url(url).build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                wrapperResponse.onFail(HttpFailTip.NETWORK_ERROR, HttpFailTip.getFailTipMessage(HttpFailTip.NETWORK_ERROR));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    try {
                        String string = response.body().string();

                        if (TextUtils.isEmpty(string)) {
                            wrapperResponse.onFail(HttpFailTip.NETWORK_ERROR, HttpFailTip.getFailTipMessage(HttpFailTip.NETWORK_ERROR));
                        } else {
                            wrapperResponse.onSuccess(null, string);
                        }
                    } catch (Exception e) {
                        wrapperResponse.onFail(HttpFailTip.NETWORK_ERROR, HttpFailTip.getFailTipMessage(HttpFailTip.NETWORK_ERROR));
                    }
                } else {
                    wrapperResponse.onFail(HttpFailTip.NETWORK_ERROR, HttpFailTip.getFailTipMessage(HttpFailTip.NETWORK_ERROR));
                }
            }
        });
    }


    /**
     * 上传文件通用方法 (OKHTTP方法）
     *
     * @param url
     * @param action
     * @param paramArr
     * @param commonResponse
     */
    public static void uploadFilesCommon(String url, String action, String paramArr, List<File> files, final CommonResponse commonResponse) {
        if (files == null || files.size() == 0) return;

        if (TextUtils.isEmpty(paramArr)) {
            paramArr = "{}";
        }

        MultipartBody.Builder builder = new MultipartBody.Builder();
        //设置类型
        builder.setType(MultipartBody.FORM);

        /**
         * 如果action 为空就不加公共参数
         * 应用场景  数据组埋点上传
         */
        if (!TextUtils.isEmpty(action)) {
            try {
//                String ency = Aes.encryptContent(getparamArr(paramArr, action), EncryptionType, KEY, ENCODING);
//                builder.addFormDataPart("request", ency);
                                builder.addFormDataPart("request", paramArr);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (File file : files) {
            if (!file.exists()) {
                continue;
            }
            builder.addFormDataPart("file", file.getName(), RequestBody.create(null, file));
        }
        // TRY 预防埋点上报 出现 IllegalStateException("Multipart body must have at least one part.")
        try {
            //创建RequestBody
            RequestBody body = builder.build();

            final OkhttpWrapperResponse wrapperResponse = new OkhttpWrapperResponse(commonResponse, sConverts, action);
            OkHttpClient client = new OkHttpClient();
            Call call;
            final Request request = new Request.Builder().
                    url(url).post(body).build();

            if ("https".equals(URI.create(url).getScheme().toLowerCase())) {
                //参数为NULL设置可访问所有的https网站 ，这里可以支持自签名证书
                HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
                call = client.newBuilder().
                        sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager).hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                }).build().newCall(request);
            } else {
                call = client.newBuilder().build().newCall(request);
            }
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    wrapperResponse.onFail(HttpFailTip.UPLOAD_ERROR, HttpFailTip.getFailTipMessage(HttpFailTip.UPLOAD_ERROR));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();

                        if (TextUtils.isEmpty(string)) {
                            wrapperResponse.onFail(HttpFailTip.NETWORK_ERROR, HttpFailTip.getFailTipMessage(HttpFailTip.NETWORK_ERROR));
                        } else {
                            wrapperResponse.onSuccess(null, string);
                        }

                    } else {
                        wrapperResponse.onFail(HttpFailTip.UPLOAD_ERROR, HttpFailTip.getFailTipMessage(HttpFailTip.UPLOAD_ERROR));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 下载文件通用方法 注意是用 DownloadCallback  并且回调是在子线程
     *
     * @param fileUrl
     * @param callback
     */
    public static void downloadFileCommon(String fileUrl, final String fileDir, final DownloadCallback callback) {
        if (TextUtils.isEmpty(fileUrl) || TextUtils.isEmpty(fileDir) || callback == null) {
            return;
        }

        final String fileName = MD5Util.MD5Encode("fileUrl",null);
        final File file = new File(fileDir, fileName);
        if (file.exists()) {
            file.delete();
        }
        final Request request = new Request.Builder().url(fileUrl).build();
        OkHttpClient client = new OkHttpClient();
        final Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFail(HttpFailTip.DOWNLOAD_ERROR, HttpFailTip.getFailTipMessage(HttpFailTip.DOWNLOAD_ERROR));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    long total = response.body().contentLength();
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                        int progress = (int) (current * 100.0 / total);
                        if (progress <= 100) {
                            callback.onProgress(progress);
                        }
                    }
                    fos.flush();
                    callback.onSuccess(file);
                } catch (IOException e) {
                    callback.onFail(HttpFailTip.DOWNLOAD_ERROR, HttpFailTip.getFailTipMessage(HttpFailTip.DOWNLOAD_ERROR));
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }


    /**
     * 注意是用 DownloadCallback  并且回调是在子线程
     *
     * @param fileUrl
     * @param callback
     * @param fileName 文件的整体路径  为了效率 不回调onProgress
     */
    public static void downloadFileByName(String fileUrl, final String fileName, final DownloadCallback callback) {
        if (TextUtils.isEmpty(fileUrl) || TextUtils.isEmpty(fileName) || callback == null) {
            return;
        }

        final File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        final Request request = new Request.Builder().url(fileUrl).build();
        OkHttpClient client = new OkHttpClient();
        final Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFail(HttpFailTip.DOWNLOAD_ERROR, HttpFailTip.getFailTipMessage(HttpFailTip.DOWNLOAD_ERROR));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    callback.onSuccess(file);
                } catch (IOException e) {
                    callback.onFail(HttpFailTip.DOWNLOAD_ERROR, HttpFailTip.getFailTipMessage(HttpFailTip.DOWNLOAD_ERROR));
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}
