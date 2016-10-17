package com.fieldnation.fnhttp;

/**
 * Created by Michael on 10/17/2016.
 */

public interface ResultListener {
    void onStart();

    void onProgress(long totalTime, long bytes);

    void onSuccess(HttpResult httpResult);

    void onFail(Exception exception);
}
