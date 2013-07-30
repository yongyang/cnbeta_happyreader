package org.jandroid.cnbeta.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class NetWorkUtils {

    public static enum ConnectionStatus {
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
            else if (networkInfo.getTypeName().toLowerCase().equals("wifi"))
                connectionStatus = ConnectionStatus.WIFI;
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

    public static void checkConnectionStatus(Context theContext){
        NetWorkUtils.ConnectionStatus connectionStatus = NetWorkUtils.getConnectionStatus(theContext);
        if(connectionStatus.equals(NetWorkUtils.ConnectionStatus.NONE)) {
            Toast.makeText(theContext, "没有网络，只能浏览离线数据", Toast.LENGTH_SHORT).show();
        }
    }
}
