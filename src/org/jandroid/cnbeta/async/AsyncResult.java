package org.jandroid.cnbeta.async;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class AsyncResult<T> {
    private boolean success = false;
    private T result;
    private Exception exception;
    private Map<String, Object> extras = new HashMap<String, Object>();

    AsyncResult(boolean success, T result) {
        this.success = success;
        this.result = result;
    }

    public static <T> AsyncResult<T> successResult(T result){
        return new AsyncResult<T>(true, result);
    }

    public static AsyncResult<String> errorResult(String errorMsg){
        return new AsyncResult<String>(false, errorMsg);
    }

    public static AsyncResult<String> errorResult(String errorMsg, Exception e){
        AsyncResult<String> errorAsyncResult = new AsyncResult<String>(false, errorMsg);
        errorAsyncResult.exception = e;
        return errorAsyncResult;
    }

    public String getErrorMsg(){
        if(success) {
            return "success";
        }
        else {
            return result.toString();
        }
    }

    public Exception getException() {
        return exception;
    }

    public T getResult() {
        return result;
    }

    public boolean isSuccess() {
        return success;
    }


    public void putExtra(String key, Object value) {
        extras.put(key, value);
    }

    public Object getExtra(String key) {
        return extras.get(key);
    }
}
