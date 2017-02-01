package com.fieldnation.v2.data.listener;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.App;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fnpigeon.TopicService;
import com.fieldnation.fnstore.StoredObject;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.StreamUtils;

import java.io.FileInputStream;

/**
 * Created by mc on 2/1/17.
 */

public class CacheDispatcher extends AsyncTaskEx<Object, Object, Bundle> {
    private static final String TAG = "CacheDispatcher";
    Context _context;
    TransactionParams _transactionParams;

    public CacheDispatcher(Context context, String key) {
        executeEx(context, key);
    }

    @Override
    protected Bundle doInBackground(Object... params) {
        try {
            _context = (Context) params[0];
            String key = (String) params[1];

            StoredObject obj = StoredObject.get(_context, App.getProfileId(), "V2_DATA", key);
            if (obj == null)
                return null;

            StoredObject paramsObj = StoredObject.get(_context, App.getProfileId(), "V2_PARAMS", key);
            if (paramsObj == null)
                return null;

            _transactionParams = TransactionParams.fromJson(new JsonObject(paramsObj.getData()));
            Bundle bundle = new Bundle();
            bundle.putParcelable("params", _transactionParams);
            bundle.putBoolean("success", true);
            if (obj.isFile()) {
                bundle.putByteArray("data", StreamUtils.readAllFromStream(new FileInputStream(obj.getFile()), (int) obj.getFile().length(), 1000));
            } else {
                bundle.putByteArray("data", obj.getData());
            }
            return bundle;
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bundle bundle) {
        if (bundle != null)
            TopicService.dispatchEvent(_context, _transactionParams.topicId, bundle, Sticky.TEMP);
    }
}
