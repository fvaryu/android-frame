package com.lowett.core.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;


import com.lowett.core.BuildConfig;
import com.lowett.core.Frame;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Hyu on 2016/7/26.
 * Email: fvaryu@qq.com
 */
public final class Utility {

    public static boolean isMobileNO(String mobile) {
        return !TextUtils.isEmpty(mobile) && mobile.startsWith("1") && mobile.length() == 11;
    }

    public static boolean isValidIdCard(String idCard) {
        if (!TextUtils.isEmpty(idCard)) {
//			Pattern pattern = Pattern.compile("/^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/");
//			Matcher matcher = pattern.matcher(idCard);
//			return matcher.matches()
        }
        return false;
        //	/^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/
    }

    public static boolean isVerCheckCode(String checkCode) {
       return !TextUtils.isEmpty(checkCode) && (checkCode.length() == 4 || checkCode.length() == 6);
    }

    public static boolean isPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 6 && password.length() <= 18;
    }

    public static boolean isValidNickName(String nickName) {
        return !TextUtils.isEmpty(nickName) && nickName.length() > 0 && nickName.length() < 10;
    }

    public static boolean isValidInviteCode(String code) {
        return !TextUtils.isEmpty(code) && code.length() == 6;
    }

    public static boolean isMunicipality(String province) {
        if ("北京市".equals(province) ||
                "重庆市".equals(province) ||
                "天津市".equals(province) ||
                "上海市".equals(province)) {
            return true;
        }
        return false;
    }

    public static boolean isObjEmpty(Object o) {
        try {
            if (o != null && !TextUtils.isEmpty(o.toString())) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static boolean isNetWorkAvailable(Context c) {
        if (!NetWrapper.isNetworkAvailable(c)) {
            Toast.makeText(c, "网络连接不可用", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public static String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    public static String mobileInfo() {
        return Build.MANUFACTURER + "," + Build.MODEL + "," + Build.VERSION.RELEASE;
    }

    public static String getDeviceId() {
        TelephonyManager manager;
        try {
            manager = (TelephonyManager) Frame.getInstance().getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
            return manager.getDeviceId();
        } catch (Exception e) {
           return NetWrapper.getMacAddress(Frame.getInstance().getAppContext());
        }
    }

    public static String sign(Map<String, String> map, String secret) throws IOException{
        try {
            StringBuilder sb = new StringBuilder();
            List<String> keys = new ArrayList<>(map.size());
            keys.addAll(map.keySet());
            Collections.sort(keys);

            sb.append(secret);
            for (String paramName : keys) {
                sb.append(paramName).append(map.get(paramName));
            }
            sb.append(secret);
            byte[] sha1Digest = getSHA1Digest(sb.toString());
            return byte2hex(sha1Digest);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    private static byte[] getSHA1Digest(String data) throws IOException {
        byte[] bytes;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            bytes = md.digest(data.getBytes("utf-8"));
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse.getMessage());
        }
        return bytes;
    }

    private static byte[] getMD5Digest(String data) throws IOException {
        byte[] bytes;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            bytes = md.digest(data.getBytes("utf-8"));
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse.getMessage());
        }
        return bytes;
    }

    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }
}
