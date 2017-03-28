package com.lowett.core.utils;


import com.lowett.core.BuildConfig;

public class Logger {
    protected static final String TAG = "tag";

    private Logger() {
    }

    /**
     * Send a VERBOSE log msg.
     *
     * @param msg The msg you would like logged.
     */
    public static void v(String msg) {
        if (BuildConfig.DEBUG)
            android.util.Log.v(TAG, buildMessage(msg));
    }

    /**
     * Send a VERBOSE log msg and log the exception.
     *
     * @param msg The msg you would like logged.
     * @param thr An exception to log
     */
    public static void v(String msg, Throwable thr) {
        if (BuildConfig.DEBUG)
            android.util.Log.v(TAG, buildMessage(msg), thr);
    }

    /**
     * Send a DEBUG log msg.
     *
     * @param msg
     */
    public static void d(String msg) {
        if (BuildConfig.DEBUG)
            android.util.Log.d(TAG, buildMessage(msg));
    }

    /**
     * Send a DEBUG log msg and log the exception.
     *
     * @param msg The msg you would like logged.
     * @param thr An exception to log
     */
    public static void d(String msg, Throwable thr) {
        if (BuildConfig.DEBUG)
            android.util.Log.d(TAG, buildMessage(msg), thr);
    }

    /**
     * Send an INFO log msg.
     *
     * @param msg The msg you would like logged.
     */
    public static void i(String msg) {
        if (BuildConfig.DEBUG)
            android.util.Log.i(TAG, buildMessage(msg));
    }

    /**
     * Send a INFO log msg and log the exception.
     *
     * @param msg The msg you would like logged.
     * @param thr An exception to log
     */
    public static void i(String msg, Throwable thr) {
        if (BuildConfig.DEBUG)
            android.util.Log.i(TAG, buildMessage(msg), thr);
    }

    /**
     * Send an ERROR log msg.
     *
     * @param msg The msg you would like logged.
     */
    public static void e(String msg) {
        if (BuildConfig.DEBUG)
            android.util.Log.e(TAG, buildMessage(msg));
    }

    /**
     * Send a WARN log msg
     *
     * @param msg The msg you would like logged.
     */
    public static void w(String msg) {
        if (BuildConfig.DEBUG)
            android.util.Log.w(TAG, buildMessage(msg));
    }

    /**
     * Send a WARN log msg and log the exception.
     *
     * @param msg The msg you would like logged.
     * @param thr An exception to log
     */
    public static void w(String msg, Throwable thr) {
        if (BuildConfig.DEBUG)
            android.util.Log.w(TAG, buildMessage(msg), thr);
    }

    /**
     * Send an empty WARN log msg and log the exception.
     *
     * @param thr An exception to log
     */
    public static void w(Throwable thr) {
        if (BuildConfig.DEBUG)
            android.util.Log.w(TAG, buildMessage(""), thr);
    }

    /**
     * Send an ERROR log msg and log the exception.
     *
     * @param msg The msg you would like logged.
     * @param thr An exception to log
     */
    public static void e(String msg, Throwable thr) {
        if (BuildConfig.DEBUG)
            android.util.Log.e(TAG, buildMessage(msg), thr);
    }

    /**
     * Building Message2
     *
     * @param msg The msg you would like logged.
     * @return Message2 String
     */
    protected static String buildMessage(String msg) {
        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];
        return new StringBuilder()
                .append(caller.getClassName())
                .append(".")
                .append(caller.getMethodName())
                .append("(")
                .append(caller.getFileName())
                .append(":")
                .append(caller.getLineNumber())
                .append(")")
                .append(msg).toString();
    }
}
