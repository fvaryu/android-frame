package com.lowett.core.net.internal;

import android.content.Context;
import android.support.annotation.UiThread;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.lowett.core.BuildConfig;
import com.lowett.core.Frame;
import com.lowett.core.net.DefaultAsyncHttpResponseHandler;
import com.lowett.core.net.IHttpDomain;
import com.lowett.core.utils.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Hyu on 2017/3/28.
 * Email: fvaryu@qq.com
 */

public abstract class BaseHttpClient {

    private static final int TIMEOUT = 10 * 1000;

    private final Gson mGson = new Gson();
    private OkHttpClient mOkHttpClient;
    private Platform mPlatform = Platform.get();
    private boolean sslEnable;

    public BaseHttpClient() {
        this(false);
    }

    public BaseHttpClient(boolean sslEnable) {
        this.sslEnable = sslEnable;
    }

    public static class Builder {
        InputStream serverCerStream;
        InputStream clientCerStream;
        String clientCerPassword;
        boolean sslEnable;

        public Builder() {
        }

        public Builder(InputStream serverCerStream, InputStream clientCerStream, String clientCerPassword) {
            super();
            this.serverCerStream = serverCerStream;
            this.clientCerStream = clientCerStream;
            this.clientCerPassword = clientCerPassword;
        }


        public Builder setServerCerStream(InputStream serverCerStream) {
            this.serverCerStream = serverCerStream;
            return this;
        }

        public Builder setClientCerStream(InputStream clientCerStream) {
            this.clientCerStream = clientCerStream;
            return this;
        }

        public Builder setClientCerPassword(String password) {
            clientCerPassword = password;
            return this;
        }

        public Builder setSslEnable(boolean sslEnable) {
            this.sslEnable = sslEnable;
            return this;
        }

        public OkHttpClient build() {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .pingInterval(6 * 1000, TimeUnit.SECONDS)
                    .cookieJar(new CookieJar() {
                        @Override
                        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                            for (Cookie cookie : cookies) {
                                if ("sid".equals(cookie.name())) {
                                    Frame.getInstance().getAppData().setSessionId(cookie.value());
                                    Frame.getInstance().getAppData().setDomain(cookie.domain());
                                    break;
                                }

                            }
                        }

                        @Override
                        public List<Cookie> loadForRequest(HttpUrl url) {
                            List<Cookie> list = new ArrayList<>(1);
                            if (!TextUtils.isEmpty(Frame.getInstance().getAppData().getSessionId())) {
                                Logger.i("local sid = " + Frame.getInstance().getAppData().getSessionId());
                                Cookie cookie = new Cookie.Builder().name("sid")
                                        .value(Frame.getInstance().getAppData().getSessionId())
                                        .domain(Frame.getInstance().getAppData().getDomain())
                                        .build();
                                list.add(cookie);
                            }
                            return list;
                        }
                    });

            if (sslEnable) {
                SSLManagerUtils.sslSocketFactory(builder, serverCerStream, clientCerStream, clientCerPassword);
            }
            return builder.build();
        }
    }

    public void initByHttps(InputStream serverInput, InputStream clientInput, String password) {
        init(new Builder(serverInput, clientInput, password)
                .setSslEnable(sslEnable));
    }

    public void initByHttp() {
        init(new Builder());
    }


    public abstract IHttpDomain getHttpDomain();

    private void init(Builder builder) {
        mOkHttpClient = builder.build();
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    protected abstract Map<String, String> addHeaders();

    private Headers headers() {
        Map<String, String> map = addHeaders();
        Headers.Builder builder = new Headers.Builder();
        if (map != null) {
            for (String key : map.keySet()) {
                String value = map.get(key);
                if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
                    continue;
                }
                builder.add(key, value);
            }
        }
        return builder.build();
    }

    @UiThread
    protected final void toPost(Context context, boolean isShow, String relativeUrl, RequestParams params,
                                IParser iParser/*如果不需要解析数据，传null*/, INetCallBack callBack) {
        if (params == null) {
            throw new RuntimeException("Http params must be not null!");
        }

        String domain = getHttpDomain().getApiDoMain();
        Headers headers = headers();
        RequestBody body;
        FormBody formBody = params.formBody();

        MultipartBody.Builder multipartBuilder = params.getMultipartBuilder();
        if (multipartBuilder != null) {
            multipartBuilder.addPart(formBody);
            body = multipartBuilder.build();
        } else {
            body = formBody;
        }

        if (BuildConfig.DEBUG) {
            Logger.i(String.format("Http url-> %s%s", domain, relativeUrl));
            Logger.i("Http headers from local to server->\n" + headers.toString());

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < formBody.size(); i++) {
                builder.append(formBody.name(i))
                        .append("=")
                        .append(formBody.value(i))
                        .append("&");
            }
            if (builder.length() > 0) {
                builder.replace(builder.length() - 1, builder.length(), "");
            }
            Logger.i(String.format("Http params -> %s", builder.toString()));
        }

        ResponseHandleInterface httpResponseHandler = getResponseHandleInterface(context, mOkHttpClient, mPlatform, isShow, context, iParser, mGson, callBack);
        httpResponseHandler.onStart();
        mOkHttpClient.newCall(new Request.Builder()
                .url(domain + relativeUrl)
                .headers(headers)
                .post(body)
                .build()).enqueue(httpResponseHandler);
    }

    private ResponseHandleInterface getResponseHandleInterface(Context context, OkHttpClient client, Platform mPlatform, boolean isShow, Object tag, IParser iParser, Gson gson, INetCallBack callBack) {
        return new DefaultAsyncHttpResponseHandler(context, client,  mPlatform, isShow, tag, iParser,gson, callBack);
    }

    public void cancelByTag(Object tag) {
        if (tag == null) return;

        for (Call runningCall :
                mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(runningCall.request().tag())) {
                runningCall.cancel();
            }
        }

        for (Call call :
                mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public void cancelAll() {
        mOkHttpClient.dispatcher().cancelAll();
    }
}
