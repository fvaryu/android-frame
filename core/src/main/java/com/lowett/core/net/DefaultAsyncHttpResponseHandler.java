package com.lowett.core.net;

import android.content.Context;
import android.content.DialogInterface;

import com.google.gson.Gson;
import com.lowett.core.net.internal.DefaultParser;
import com.lowett.core.net.internal.INetCallBack;
import com.lowett.core.net.internal.IParser;
import com.lowett.core.net.internal.Platform;
import com.lowett.core.net.internal.ResponseData;
import com.lowett.core.net.internal.ResponseHandleInterface;
import com.lowett.core.utils.Logger;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by Hyu on 2016/7/11.
 * Email: fvaryu@qq.com
 */
public class DefaultAsyncHttpResponseHandler implements ResponseHandleInterface {

    private LoadingDialog mLoadingDialog;
    private Context mContext;
    private ParserResult mParserResult = new ParserResult();
    private boolean isShow;
    private IParser mIParser;
    private INetCallBack mCallBack;
    private Object tag;
    private Platform mPlatform;
    private Gson mGson;
    private OkHttpClient mOkHttpClient;

    public DefaultAsyncHttpResponseHandler(Context context, OkHttpClient client, Platform platform, boolean isShow, Object tag, IParser iParser, Gson gson, INetCallBack callBack) {
        this.mContext = context;
        this.isShow = isShow;
        this.mIParser = iParser;
        this.mCallBack = callBack;
        this.tag = tag;
        this.mPlatform = platform;
        this.mGson = gson;
        this.mOkHttpClient = client;
        if (mGson == null) {
            this.mGson = new Gson();
        }
    }

    @Override
    public void onStart() {
        if (!isShow) return;

        mLoadingDialog = new LoadingDialog(mContext);
        mLoadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                /**
                 * 如果是网络请求完成，dialog消失不在去调用取消请求 isShow = false
                 * 如果是用户点击back/屏幕，要请求cancel request， isShow = true
                 * {@link DefaultAsyncHttpResponseHandler#dismiss()}
                 */
                if (!isShow) {
                    return;
                }
               cancelByTag(tag);
            }
        });
        mLoadingDialog.show();
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

    private void dismiss() {
        if (!isShow || mLoadingDialog == null) {
            return;
        }
        isShow = false;
        mLoadingDialog.dismiss();
    }


    @Override
    public void onFailure(Call call, IOException e) {
        if (call.isCanceled()) {
            Logger.i("Http is Canceled ,but fail response back, not deal");
            return;
        }
        Logger.i(e.getMessage());
        mParserResult.res = new ResponseData(e.getMessage());
        finished();
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        if (call.isCanceled()) {
            Logger.i("Http is Canceled ,but response back, not deal");
            return;
        }
        if (response.isSuccessful()) {
            String resData = response.body().string();
            Logger.i("response header:" + response.headers().toString());
            Logger.i(String.format("response data:\n%s", resData));
            ResponseData res = new DefaultParser().parser(mGson, response.code(), response.headers(), resData);
            mParserResult.res = res;
            if (mIParser != null && !(mIParser instanceof DefaultParser) && res.isSuccess()) {
                mParserResult.mT = mIParser.parser(mGson, response.code(), response.headers(), resData);
            }
            finished();
        } else {
            onFailure(call, new IOException("http is fail, code=" + response.code() + " ,message=" + response.message()));
        }
    }

    @SuppressWarnings("unchecked")
    private void finished() {

        mPlatform.execute(new Runnable() {

            @Override
            public void run() {
                if (mCallBack != null) {
                    if (mParserResult.res == null) {
                        mParserResult.res = new ResponseData();
                    }

                    mCallBack.onFinish(mParserResult.res, mParserResult.mT);
                }

                dismiss();
            }
        });
    }


    private class ParserResult<T> {
        public ResponseData res = null;
        public T mT;
    }
}
