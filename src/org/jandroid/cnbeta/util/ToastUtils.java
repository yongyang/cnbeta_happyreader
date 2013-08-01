package org.jandroid.cnbeta.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 8/1/13 3:52 PM
 */
public class ToastUtils {
    
    public static Toast noNetwork(Context theContext){
        return Toast.makeText(theContext, "没有网络，只能浏览离线数据", Toast.LENGTH_SHORT);
    }
    
    public static Toast noSdCard(Context theContext){
        return Toast.makeText(theContext, "没有SD卡，将使用手机内置存储器", Toast.LENGTH_SHORT);
    }
}
