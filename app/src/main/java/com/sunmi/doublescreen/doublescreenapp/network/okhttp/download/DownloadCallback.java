package com.sunmi.doublescreen.doublescreenapp.network.okhttp.download;

import java.io.File;

/**
 * Created by hua on 2017/1/19.
 */

public interface DownloadCallback {

    void onSuccess(File file);

    void onFail(int errorCode, String errorMessage);

    void onProgress(int progress);
}
