package org.jandroid.common;

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

    public static void showShortToast(Context theContext, String info){
        Toast.makeText(theContext, info, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context theContext, String info){
        Toast.makeText(theContext, info, Toast.LENGTH_LONG).show();
    }

}
