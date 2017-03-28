package com.lowett.core.net.internal;

import com.google.gson.Gson;

import org.greenrobot.greendao.annotation.NotNull;

import okhttp3.Headers;
//import okhttp3.internal.frame.Header;

/**
 * Created by Hyu on 2016/7/11.
 * Email: fvaryu@qq.com
 */
public abstract class IParser<T> {
    public abstract T parser(@NotNull Gson gson, int resultCode, Headers headers, String response);
}
