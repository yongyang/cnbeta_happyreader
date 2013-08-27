package org.jandroid.common.async;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class AsyncResult<T> {
    private boolean success = false;
    private T result;
    private String errorMsg;
    private Exception exception;
    private Map<String, Object> extras = new HashMap<String, Object>();

    AsyncResult(boolean success, T result) {
        this.success = success;
        this.result = result;
    }

    AsyncResult(boolean success, T result, String errorMsg) {
        this.success = success;
        this.result = result;
        this.errorMsg = errorMsg;
    }

    AsyncResult(boolean success, T result, Exception e) {
        this.success = success;
        this.result = result;
        this.errorMsg = errorMsg;
    }

    public static <T> AsyncResult<T> successResult(T result){
        return new AsyncResult<T>(true, result);
    }

    public static <T> AsyncResult<T> errorResult(String errorMsg){
        return new AsyncResult<T>(false, null);
    }

    public static <T> AsyncResult<T> errorResult(String errorMsg, T result, Exception e){
        AsyncResult<T> errorAsyncResult = new AsyncResult<T>(false, result, e);
        errorAsyncResult.exception = e;
        return errorAsyncResult;
    }

    public String getErrorMsg(){
        if(success) {
            return null;
        }
        else {
            //TODO: result may be null
            if(errorMsg != null) {
                return errorMsg;
            }
            else if(exception != null) {
                return exception.toString();
            }
            return "error";
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
