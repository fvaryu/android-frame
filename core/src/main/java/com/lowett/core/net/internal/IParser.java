package com.lowett.core.net.internal;

import android.support.annotation.NonNull;

import com.google.gson.Gson;


import okhttp3.Headers;
//import okhttp3.internal.frame.Header;

/**
 * Created by Hyu on 2016/7/11.
 * Email: fvaryu@qq.com
 */
public abstract class IParser<T> {
    public abstract T parser(@NonNull Gson gson, int resultCode, Headers headers, String response);
}
