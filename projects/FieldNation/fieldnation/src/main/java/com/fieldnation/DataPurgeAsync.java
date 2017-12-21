package com.fieldnation;

import android.content.Context;
import android.content.SharedPreferences;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.DebugUtils;

import java.util.List;

/**
 * Created by mc on 12/19/17.
 */

public class DataPurgeAsync extends AsyncTaskEx<Object, Object, Object> {
    private static final String TAG = "DataPurgeAsync";

    private static final long ONE_DAY = 86400000;
    private static final long ONE_WEEK = 604800000;
    private Listener _listener;

    @Override
    protected Object doInBackground(Object... params) {
        Context context = (Context) params[0];
        _listener = (Listener) params[1];

        SharedPreferences settings = context.getSharedPreferences(
                context.getPackageName() + "_preferences",
                Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);

        long _imageDaysToLive = Integer.parseInt(settings.getString(context.getString(R.string.pref_key_remove_rate), "-1")) * 2;

        Log.v(TAG, "Flushing logs");
        DebugUtils.flushLogs(context, ONE_DAY); // 1 day
        Log.v(TAG, "Flushing data");
        StoredObject.flush(App.get(), ONE_WEEK); // 1 week
        //StoredObject.flush(1000); // 1 week

        Log.v(TAG, "_imageDaysToLive: " + _imageDaysToLive + " haveWifi: " + App.get().haveWifi());
        // only flush if we have wifi, so that the app can get new ones without
        // worrying about cell traffic
        if (_imageDaysToLive > -1 && App.get().haveWifi()) {
            long days = _imageDaysToLive * ONE_DAY;
            long cutoff = System.currentTimeMillis();
            List<StoredObject> list = StoredObject.list(App.get(), App.getProfileId(), "PhotoCache");

            Log.v(TAG, "Flushing photos");
            int count = 0;
            for (StoredObject obj : list) {
                if (obj.getLastUpdated() + days < cutoff) {
                    StoredObject.delete(App.get(), obj);
                    count++;
                }
            }

            list = StoredObject.list(App.get(), App.getProfileId(), "PhotoCacheCircle");

            for (StoredObject obj : list) {
                if (obj.getLastUpdated() + days < cutoff) {
                    StoredObject.delete(App.get(), obj);
                    count++;
                }
            }

            Log.v(TAG, "Flushing photos " + count);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (_listener != null)
            _listener.onFinish();
    }

    public DataPurgeAsync run(Context context, Listener listener) {
        return (DataPurgeAsync) executeEx(context, listener);
    }

    public interface Listener {
        void onFinish();
    }
}
