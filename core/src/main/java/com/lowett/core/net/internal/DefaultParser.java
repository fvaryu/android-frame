package com.lowett.core.net.internal;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import okhttp3.Headers;

/**
 * Created by Hyu on 2016/7/11.
 * Email: fvaryu@qq.com
 */
public class DefaultParser extends IParser {

    @Override
    public ResponseData parser(Gson gson, int resultCode, Headers headers, String response) {
        ResponseData res;
        try {
            res = gson.fromJson(response, ResponseData.class);
        } catch (JsonSyntaxException e) {
            res = new ResponseData();
        }
        return res;
    }
}
