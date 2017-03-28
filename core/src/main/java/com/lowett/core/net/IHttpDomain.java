package com.lowett.core.net;


/**
 * Created by Hyu on 2016/7/11.
 * Email: fvaryu@qq.com
 */
public interface IHttpDomain {


    String getApiDoMain();

    String getWSDomain();

    void setIsOnline(boolean online);

    boolean isOnline();

    void setApiDefine(String apiDefine);

    void setWsDefine(String wsDefine);
}
