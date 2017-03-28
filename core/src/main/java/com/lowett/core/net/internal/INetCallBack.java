package com.lowett.core.net.internal;



/**
 * Created by Hyu on 2016/7/11.
 * Email: fvaryu@qq.com
 */
public interface INetCallBack<T> {
    void onFinish(ResponseData response, T t);
}
