package org.jandroid.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class EnvironmentUtils {
    
    public static enum ConnectionStatus {
        MOBILE("MOBILE"),
        WIFI("WIFI"),
        WAP("WAP"),
        NET("NET"),
        NONE("NONE");

        private String name;

        private ConnectionStatus(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static ConnectionStatus getConnectionStatus(Context theContext) {
        ConnectionStatus connectionStatus;
        try {
            NetworkInfo networkInfo = ((ConnectivityManager) theContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if(!networkInfo.isAvailable()) {
                connectionStatus = ConnectionStatus.NONE;
            }
            else if(!networkInfo.isConnected()) {
                connectionStatus = ConnectionStatus.NONE;
            }
            else if (networkInfo.getTypeName().toLowerCase().equals("wifi"))
                connectionStatus = ConnectionStatus.WIFI;
            else if (networkInfo.getTypeName().toLowerCase().equals("mobile"))
                connectionStatus = ConnectionStatus.MOBILE;
            else if (networkInfo.getExtraInfo().toLowerCase().contains("wap"))
                connectionStatus = ConnectionStatus.WAP;
            else if (networkInfo.getExtraInfo() == null)
                connectionStatus = ConnectionStatus.NET;
            else
                connectionStatus = ConnectionStatus.NET;
        }
        catch (Exception e) {
            connectionStatus = ConnectionStatus.NONE;
        }
        return connectionStatus;
    }
    
    public static boolean checkNetworkConnected(Context theContext){
        ConnectionStatus connectionStatus = getConnectionStatus(theContext);
        return !(connectionStatus.equals(ConnectionStatus.NONE) || connectionStatus.equals(ConnectionStatus.WAP));
    }

    public static boolean checkMobileNetworkConnected(Context theContext) {
        ConnectionStatus connectionStatus = getConnectionStatus(theContext);
        return connectionStatus.equals(ConnectionStatus.MOBILE);
    }


    public static boolean checkWIFINetworkConnected(Context theContext) {
        ConnectionStatus connectionStatus = getConnectionStatus(theContext);
        return connectionStatus.equals(ConnectionStatus.WIFI);
    }


    /**
     * 检测sdcard是否可用
     *
     * @return true为可用，否则为不可用
     */
    public static boolean checkSdCardMounted(Context theContext) {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

   	/**
   	 * Checks if there is enough Space on SDCard
   	 *
   	 * @param expectedSize
   	 *            Size to Check
   	 * @return True if the Update will fit on SDCard, false if not enough space on SDCard Will also return false, if the SDCard is
   	 *         not mounted as read/write
   	 */
   	public static boolean hasEnoughSpaceOnSdCard(long expectedSize) {
   		String status = Environment.getExternalStorageState();
   		if (!status.equals(Environment.MEDIA_MOUNTED))
   			return false;
   		return (expectedSize < getAvailableSpaceOnSdCard());
   	}

   	/**
   	 * get the space is left over on sdcard
   	 */
   	public static long getAvailableSpaceOnSdCard() {
   		File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
   		StatFs stat = new StatFs(path.getPath());
   		long blockSize = stat.getBlockSize();
   		long availableBlocks = stat.getAvailableBlocks();
   		return availableBlocks * blockSize;
   	}

   	/**
   	 * Checks if there is enough Space on phone self
   	 *
   	 */
   	public static boolean hasEnoughSpaceOnPhone(long expectedSize) {
   		return getAvailableSizeOnPhone() > expectedSize;
   	}

   	/**
   	 * get the space is left over on phone self
   	 */
   	public static long getAvailableSizeOnPhone() {
   		File path = Environment.getDataDirectory();
   		StatFs stat = new StatFs(path.getPath());
   		long blockSize = stat.getBlockSize();
   		long availableBlocks = stat.getAvailableBlocks();
   		long realSize = blockSize * availableBlocks;
   		return realSize;
   	}

   	/**
   	 * 根据手机分辨率从dp转成px
   	 * @param context
   	 * @param dpValue
   	 * @return
   	 */
   	public static  int dip2px(Context context, float dpValue) {
           final float scale = context.getResources().getDisplayMetrics().density;
           return (int) (dpValue * scale + 0.5f);
       }

       /**
        * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
        */
   	public static  int px2dip(Context context, float pxValue) {
           final float scale = context.getResources().getDisplayMetrics().density;
           return (int) (pxValue / scale + 0.5f)-15;
       }


}
