package org.jandroid.common;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class Logger {

    public static boolean isLogEnable = true;

    private String tag = "";

    private Logger(String tag) {
        this.tag = tag;
    }

    public static Logger newLogger(Class clz){
        return new Logger(clz.getSimpleName());
    }

    public static Logger newLogger(String tag){
        return new Logger(tag);
    }

   	public void v(String msg) {
   		if (isLogEnable)
   			android.util.Log.v(tag, msg);
   	}

   	public void v(String msg, Throwable t) {
   		if (isLogEnable)
   			android.util.Log.v(tag, msg, t);
   	}

   	public void d(String msg) {
   		if (isLogEnable)
   			android.util.Log.d(tag, msg);
   	}

   	public void d(String msg, Throwable t) {
   		if (isLogEnable)
   			android.util.Log.d(tag, msg, t);
   	}

   	public void i(String msg) {
   		if (isLogEnable)
   			android.util.Log.i(tag, msg);
   	}

   	public void i(String msg, Throwable t) {
   		if (isLogEnable)
   			android.util.Log.i(tag, msg, t);
   	}

   	public void w(String msg) {
   		if (isLogEnable)
   			android.util.Log.w(tag, msg);
   	}

   	public void w(String msg, Throwable t) {
   		if (isLogEnable)
   			android.util.Log.w(tag, msg, t);
   	}

   	public void e(String msg) {
   		if (isLogEnable)
   			android.util.Log.e(tag, msg);
   	}

   	public void e(String msg, Throwable t) {
   		if (isLogEnable)
   			android.util.Log.e(tag, msg, t);
   	}
}
