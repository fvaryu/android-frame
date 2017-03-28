package com.lowett.core.store;


/**
 * Created by Hyu on 2016/11/22.
 * Email: fvaryu@qq.com
 */

public interface IAppData {
    String APP_CONFIG_PREFS_FILE = "shared_prefs_config";

    /************
     * for user
     ************/
    long getUserId();

    void setUserId(long userId);

    String getSessionId();

    void setSessionId(String sessionId);

    String getDomain();

    void setDomain(String domain);

    boolean isBleScanEnable();

    void setBleScanEnable(boolean isBleScanEnable);

    void clean();

    /************
     * for app
     ************/

    boolean isFirstInstall();

    void setFirstInstall(boolean firstInstall);
}
