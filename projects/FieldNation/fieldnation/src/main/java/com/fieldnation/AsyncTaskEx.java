package com.fieldnation;

import android.os.AsyncTask;
import android.os.Build;

public abstract class AsyncTaskEx<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    public AsyncTask<Params, Progress, Result> executeEx(Params... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            return super.execute(params);
        }
    }
}
