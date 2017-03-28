package com.lowett.core.net.internal;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Hyu on 2016/7/11.
 * Email: fvaryu@qq.com
 */
public class RequestParams implements Serializable {
    private  MultipartBody.Builder mMultipartBuilder;
    private Map<String, String> formParams;

    public RequestParams() {
        formParams = new HashMap<>();
    }

    public RequestParams(String key, String value) {
        formParams = new HashMap<>();
        formParams.put(key, value);
    }

    public RequestParams(Map<String, String> map) {
        this();
        formParams.putAll(map);
    }

    public RequestParams put(String key, String value) {
        if (key != null && value != null) {
            formParams.put(key, value);
        }
        return this;
    }

    public RequestParams put(Map<String, String> map) {
        formParams.putAll(map);
        return this;
    }

    public RequestParams put(String key, long value) {
        return put(key, String.valueOf(value));
    }

    public RequestParams put(String key, int value) {
        return put(key, String.valueOf(value));
    }

    public RequestParams put(String key, float value) {
        return put(key, String.valueOf(value));
    }

    public FormBody formBody() {
        FormBody.Builder builder = new FormBody.Builder();
        if (formParams != null) {
            for (String key: formParams.keySet()) {
                builder.add(key, formParams.get(key));
            }
        }
        return builder.build();
    }

    public Map<String, String> getMap() {
        return formParams;
    }

    public RequestParams putFile(String key, String absolutePath) {
        if (mMultipartBuilder == null) {
            mMultipartBuilder = new MultipartBody.Builder();
        }
        File tmp = new  File(absolutePath);
        if (tmp.exists() && tmp.isFile()) {
            mMultipartBuilder.addFormDataPart(key, tmp.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), tmp));
        }
        return this;
    }

    public  MultipartBody.Builder getMultipartBuilder() {
        if (mMultipartBuilder != null) {
            mMultipartBuilder.setType(MultipartBody.FORM);
        }
        return mMultipartBuilder;
    }

}
