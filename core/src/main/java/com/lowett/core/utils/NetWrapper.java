package com.lowett.core.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public final class NetWrapper {
	
	public static boolean isNetworkAvailable(Context cxt) {
        ConnectivityManager m = (ConnectivityManager) cxt.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (m == null)
            return false;

        NetworkInfo activeNet = m.getActiveNetworkInfo();
        return activeNet != null && activeNet.isAvailable() && activeNet.isConnected();
    }

    public static  String getMacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        try {
           return wifiManager.getConnectionInfo().getMacAddress();
        } catch (Exception e) {
            return "";
        }
    }

}
