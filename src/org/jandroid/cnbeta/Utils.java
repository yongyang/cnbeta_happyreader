package org.jandroid.cnbeta;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class Utils {

    public static NetworkState GetNetworkState(Context paramContext) {
        NetworkState networkState;
        try {
            NetworkInfo networkInfo = ((ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if(!networkInfo.isAvailable()) {
                networkState = NetworkState.NONE;
            }
            else if (networkInfo.getTypeName().toLowerCase().equals("wifi"))
                networkState = NetworkState.WIFI;
            else if (networkInfo.getExtraInfo().toLowerCase().contains("wap"))
                networkState = NetworkState.WAP;
            else if (networkInfo.getExtraInfo() == null)
                networkState = NetworkState.NET;
            else
                networkState = NetworkState.NET;
        }
        catch (Exception e) {
            networkState = NetworkState.NONE;
        }
        return networkState;
    }

    public static enum NetworkState {
        WIFI("WIFI"),
        WAP("WAP"),
        NET("NET"),
        NONE("NONE");

        private String name;

        private NetworkState(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
