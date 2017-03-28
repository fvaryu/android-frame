package com.lowett.core.net.internal;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.fit.android.utils.file.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Hyu on 2016/8/26.
 * Email: fvaryu@qq.com
 */
public class DownLoadTask extends AsyncTask<String, Integer, File> {

    private String fileName;
    private DownloadFileTaskFinishListener mDownFileTaskFinishListener;

    public interface DownloadFileTaskFinishListener {
        void onDownloadFinished(File file);
    }

    public static class FileInfo {
        public String fileName;
        public String url;

        public FileInfo(String fileName, String url) {
            this.fileName = fileName;
            this.url = url;
        }

        public boolean isValidInfo() {
            return !TextUtils.isEmpty(fileName) && !TextUtils.isEmpty(url);
        }
    }

    public void startDownload(FileInfo info, DownloadFileTaskFinishListener listener) {
        if (info == null || !info.isValidInfo()) {
            throw new IllegalArgumentException("参数 无效");
        }
        mDownFileTaskFinishListener = listener;
        fileName = info.fileName;
        execute(info.url);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    //下载apk代码
    private File downLoadApk(String path, String downloadUrl) {
        File apkFile = FileUtil.getApkFile("_app.tmp");
        if (apkFile == null) {
            return null;
        }
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();

        FileOutputStream fos = null;
        InputStream is = null;
        try {
            Response response = new OkHttpClient().newCall(request).execute();
            if (!response.isSuccessful()) {
                return null;
            }
            long total = response.body().contentLength();
            is = response.body().byteStream();
            byte[] buff = new byte[1024 * 2];
            int len;
            long sum = 0;
            int lastProgress = 0;

            fos = new FileOutputStream(apkFile);
            while ((len = is.read(buff)) != -1) {
                fos.write(buff, 0, len);
                sum += len;

                int currentProgress = (int) (sum * 100 / total);
                if (lastProgress != currentProgress) {
                    lastProgress = currentProgress;
                    publishProgress(lastProgress);
                }
            }
            fos.flush();
            if (total == sum) {
                File newName = new File(path);
                if (apkFile.renameTo(newName)) {
                    return newName;
                }
            }

        } catch (Exception e) {

        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {

                }
            }

            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    ;

    @Override
    protected File doInBackground(String... params) {
        // 如果此版本的名字已经存在 不下载
        String path = FileUtil.getApkAbsolutePath(fileName);
        boolean isExit = FileUtil.isFileExist(path);
        if (isExit) {
            return new File(path);
        }
        return downLoadApk(path, params[0]);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(File f) {
        super.onPostExecute(f);
        if (mDownFileTaskFinishListener != null) {
            mDownFileTaskFinishListener.onDownloadFinished(f);
        }
    }
}