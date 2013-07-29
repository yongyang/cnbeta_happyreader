package org.jandroid.cnbeta.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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

    public static ConnectionStatus getConnectionStatus(Context paramContext) {
        ConnectionStatus connectionStatus;
        try {
            NetworkInfo networkInfo = ((ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
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

}
