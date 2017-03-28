package com.lowett.core.crashmanager;

import android.text.TextUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.UUID;

class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler previousHandler;
    private String path;

    ExceptionHandler(Thread.UncaughtExceptionHandler handler, String path) {
        this.previousHandler = handler;
        this.path = path;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        final Date now = new Date();
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);

        exception.printStackTrace(printWriter);

        try {

            String logDir = path;
            if (TextUtils.isEmpty(logDir)) {
                return;
            }

            String filename = UUID.randomUUID().toString();
            String path = logDir + File.separator + filename + ".stacktrace";

            BufferedWriter write = new BufferedWriter(new FileWriter(path));
            write.write("Package: " + CrashManagerConstants.APP_PACKAGE + "\n");
            write.write("Version: " + CrashManagerConstants.APP_VERSION + "\n");
            write.write("Android: " + CrashManagerConstants.ANDROID_VERSION + "\n");
            write.write("Manufacturer: " + CrashManagerConstants.PHONE_MANUFACTURER + "\n");
            write.write("Model: " + CrashManagerConstants.PHONE_MODEL + "\n");
            write.write("Date: " + now + "\n");
            write.write("\n");
            write.write(result.toString());
            write.flush();
            write.close();
        } catch (Exception another) {

        } finally {
            previousHandler.uncaughtException(thread, exception);
        }

    }
}