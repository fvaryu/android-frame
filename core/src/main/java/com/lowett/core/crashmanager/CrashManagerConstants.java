package com.lowett.core.crashmanager;


import com.lowett.core.BuildConfig;

interface CrashManagerConstants {
    String APP_VERSION = String.valueOf(BuildConfig.VERSION_CODE);
    String APP_PACKAGE = String.valueOf(BuildConfig.APPLICATION_ID);
    String ANDROID_VERSION = android.os.Build.VERSION.RELEASE;
    String PHONE_MODEL = android.os.Build.MODEL;
    String PHONE_MANUFACTURER = android.os.Build.MANUFACTURER;

}
