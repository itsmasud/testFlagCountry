package com.fieldnation.v2.data.listener;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.App;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.Stopwatch;
import com.fieldnation.fntools.StreamUtils;

/**
 * Created by mc on 2/1/17.
 */

public class CacheDispatcher extends AsyncTaskEx<Object, Object, Bundle> {
    private static final String TAG = "CacheDispatcher";
    Context _context;
    TransactionParams _transactionParams;
    String topicId;

    public CacheDispatcher(Context context, String key, String topicId) {
        this.topicId = topicId;
        executeEx(context, key);
    }

    @Override
    protected Bundle doInBackground(Object... params) {
        Stopwatch stopwatch = new Stopwatch(true);
        try {
            _context = (Context) params[0];
            String key = (String) params[1];

            StoredObject obj = StoredObject.get(_context, App.getProfileId(), "V2_DATA", key);
            if (obj == null)
                return null;

            StoredObject paramsObj = StoredObject.get(_context, App.getProfileId(), "V2_PARAMS", key);
            if (paramsObj == null)
                return null;

            JsonObject p = new JsonObject(paramsObj.getData());

            _transactionParams = TransactionParams.fromJson(p.getJsonObject("transactionParams"));
            _transactionParams.topicId = topicId;
            Bundle bundle = new Bundle();
            bundle.putParcelable("params", _transactionParams);
            if (p.has("uuid"))
                bundle.putParcelable("uuid", UUIDGroup.fromJson(p.getJsonObject("uuid")));
            else
                bundle.putParcelable("uuid", (UUIDGroup) null);
            bundle.putBoolean("success", true);
            if (obj.isUri()) {
                bundle.putByteArray("data", StreamUtils.readAllFromStream(_context.getContentResolver().openInputStream(obj.getUri()), (int) obj.size(), 1000));
            } else {
                bundle.putByteArray("data", obj.getData());
            }
            bundle.putString("type", "complete");
            bundle.putBoolean("cached", true);
            return bundle;
        } catch (Exception ex) {
            Log.v(TAG, ex);
        } finally {
            if (_transactionParams != null && _transactionParams.apiFunction != null)
                Log.v(TAG, _transactionParams.apiFunction + " time: " + stopwatch.finish());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bundle bundle) {
        if (bundle != null && _transactionParams != null) {
            Log.v(TAG, "Data cache hit!");
            PigeonRoost.sendMessage(_transactionParams.topicId, bundle, Sticky.NONE);
        } else {
            Log.v(TAG, "Data Cache Miss");
        }
    }
}
