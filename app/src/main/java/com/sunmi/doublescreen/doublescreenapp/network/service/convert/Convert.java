package com.sunmi.doublescreen.doublescreenapp.network.service.convert;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by hua on 2017/1/10.
 */

public interface Convert {

    boolean isCanParse(String contentType);

    Object parse(String content, Type type) throws IOException;
}
