package com.sunmi.doublescreen.doublescreenapp.network.service.convert;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by hua on 2017/1/10.
 */

public class JsonConvert implements Convert {
    private Gson gson = new Gson();

    private static final String CONTENT_TYPE = "application/json";

    @Override
    public boolean isCanParse(String contentType) {
        if(TextUtils.isEmpty(contentType)) return false;
        return contentType.contains(CONTENT_TYPE);
    }

    @Override
    public Object parse(String content, Type type) throws IOException {
        return gson.fromJson(content, type);
    }
}
