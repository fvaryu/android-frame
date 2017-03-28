package com.lowett.core;

import android.content.Context;
import android.support.annotation.NonNull;


import com.lowett.core.store.IAppData;
import com.lowett.core.utils.Logger;

import java.lang.ref.WeakReference;

/**
 * Created by Hyu on 15/10/16.
 * Email: fvaryu@qq.com
 * <p/>
 * 底层框架全局，单例
 * 框架层的context；
 */
public final class Frame {
    private static WeakReference<Context> mWeakReference;
    private static Frame self = null;
    private IAppData mAppData;

    private boolean isInited;

    @NonNull
    public Context getAppContext() {
        return mWeakReference.get();
    }

    private void setAppContext(Context ctx) {
        if (ctx != null) {
            mWeakReference = new WeakReference<>(ctx);
        }
    }

    private Frame() {
        super();
    }

    public synchronized static Frame getInstance() {
        if (self == null) {
            self = new Frame();
        } else if (!self.isInited && mWeakReference != null) {
            self.init(mWeakReference.get());
        }

        return self;
    }

    /**
     * 上层初始化
     */
     public synchronized static Frame getInstanceWithInit(Context context) {
        if (self == null) {
            self = new Frame();
            self.init(context);
        }

        return self;
    }

    /**
     * 1、自己初始化
     * 2、三方sdk初始化
     */
    public synchronized boolean init(Context context) {
        if (isInited)
            return true;

        if (context == null) {
            return false;
        }

        Logger.i("Frame init -> " + getClass().getName());

        isInited = true;
        setAppContext(context);

        return true;
    }



    public synchronized void exit() {
        if (self != null) {
            self.release();
            self.isInited = false;
        }
    }

    public IAppData getAppData() {
        return mAppData;
    }

    public void setAppData(IAppData appData) {
        mAppData = appData;
    }

    private void release() {

    }

}
