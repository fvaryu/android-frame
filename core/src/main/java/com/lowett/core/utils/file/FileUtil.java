package com.lowett.core.utils.file;

import android.os.Environment;
import android.text.TextUtils;


import com.lowett.core.Frame;
import com.lowett.core.utils.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public final class FileUtil {

	private static String DIR_LOG = "crashLog";
	private static String TMP_LOG = "tmp";

	/**
	 * 检测sdcard是否可用
	 *
	 * @return true为可用，否则为不可用
	 */
	public static boolean isSDCardAvailable() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED))
			return false;
		return true;
	}

	public static boolean isCheckSDCardWarning() {
		if (isSDCardAvailable())
			return false;
		return true;
	}

	public static boolean createDir(String path) {
		if (isCheckSDCardWarning()) {
			return false;
		}
		if (TextUtils.isEmpty(path)) {
			Logger.e("empty path");
			return false;
		}

		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		return true;
	}

	public static File createFile(String path, String filename) {
		if (!createDir(path)) {
			return null;
		}

		if (TextUtils.isEmpty(filename)) {
			Logger.e("empty filename");
			return null;
		}

		File file = null;
		file = new File(path, filename);
		if (file.exists()) {
			return file;
		}

		try {
			file.createNewFile();
		} catch (IOException e) {
			Logger.d(e.getMessage());
			return null;
		}

		return file;
	}

	public static File createFile(String absolutePath) {
		if (TextUtils.isEmpty(absolutePath)) {
			Logger.e("empty filename");
			return null;
		}

		if (isCheckSDCardWarning()) {
			return null;
		}

		File file = new File(absolutePath);
		if (file.exists()) {
			return file;
		} else {
			File dir = file.getParentFile();
			if (!dir.exists()) {
				dir.mkdirs();
			}

			try {

				file.createNewFile();
			} catch (IOException e) {
				Logger.d(e.getMessage());
				return null;

			}
		}
		return file;
	}

	public static boolean isFileExist(String filePath) {
		if (TextUtils.isEmpty(filePath))
			return false;

		File file = new File(filePath);
		if (file != null && file.exists() && file.isFile())
			return true;
		return false;
	}

	public static void deleteFile(String filePath) {
		if (TextUtils.isEmpty(filePath))
			return;

		File file = new File(filePath);
		if (file != null && file.exists()) {
			file.delete();
			file = null;
		}
	}
	
	public static boolean objToFile(Object obj, String fileName) {
		File file = null;
		ObjectOutputStream oos = null;
		try {
			file = new File(FileUtil.getSDCardAppCachePath() + File.separator + fileName);
			if(file.exists()) {
				file.delete();
			}
			file.createNewFile();
			
			oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(obj);
			oos.flush();
		}catch (IOException e) {
			return false;
		}finally {
			try {
				if(oos != null) {
					oos.close();
				}
			} catch (Exception e) {
			}
		}
		return true;
	}

	public static Object objFromFile(String fileName) {
		File file = null;
		ObjectInputStream ois = null;
		try {
			file = new File(FileUtil.getSDCardAppCachePath() + File.separator + fileName);
			if(!file.exists()) {
				return null;
			}
			
			ois = new ObjectInputStream(new FileInputStream(file));
			return ois.readObject();
		}catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}finally {
			try {
				if(ois != null) {
					ois.close();
				}
			} catch (Exception e) {
			}
		}
	}
	
	public static boolean deleteObjFromFile(String fileName) {
		File file = null;
		file = new File(FileUtil.getSDCardAppCachePath() + File.separator + fileName);
		if(file != null && file.exists()) {
			return file.delete();
		}
		return false;
	}

	@SuppressWarnings("resource")
	public static boolean fileCopy(String dstFilepath, String srcFilepath) {
		int length = 1048891;
		FileChannel inC = null;
		FileChannel outC = null;
		try {
			FileInputStream in = new FileInputStream(srcFilepath);
			FileOutputStream out = new FileOutputStream(dstFilepath);
			inC = in.getChannel();
			outC = out.getChannel();
			ByteBuffer b = null;
			while (inC.position() < inC.size()) {
				if ((inC.size() - inC.position()) < length) {
					length = (int) (inC.size() - inC.position());
				} else {
					length = 1048891;
				}
				b = ByteBuffer.allocateDirect(length);
				inC.read(b);
				b.flip();
				outC.write(b);
				outC.force(false);
			}

			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (inC != null && inC.isOpen()) {
					inC.close();
				}
				if (outC != null && outC.isOpen()) {
					outC.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 程序sdcard目录
	public static String getSDCardAppCachePath() {
		return Frame.getInstance().getAppContext().getExternalCacheDir().getAbsolutePath();
	}

	public static String getSDCardAppAudioPath() {
		return Frame.getInstance().getAppContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath();
	}

	public static String getSDCardAppImagePath() {
		return Frame.getInstance().getAppContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
	}

	public static String getSDCardImagePath() {
		return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
	}

	public static File getApkFile(String fileName) {
		return FileUtil.createFile(FileUtil.getSDCardAppCachePath(), fileName);
	}

	public static String getApkAbsolutePath(String fileName) {
		return FileUtil.getSDCardAppCachePath() + File.separator +  fileName;
	}

	public static String getLogDir() {
		String path = getSDCardAppCachePath() + File.separator + DIR_LOG;
		if (createDir(path)) {
			return path;
		}
		return "";
	}

	public static String getTmpDir() {
		String path = getSDCardAppCachePath() + File.separator + TMP_LOG;
		if (createDir(path)) {
			return path;
		}
		return "";
	}

}
