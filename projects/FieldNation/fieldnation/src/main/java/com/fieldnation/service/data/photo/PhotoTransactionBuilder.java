package com.fieldnation.service.data.photo;

import android.content.Context;

import com.fieldnation.fnlog.Log;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionService;

/**
 * Created by Michael Carver on 4/22/2015.
 */
public class PhotoTransactionBuilder implements PhotoConstants {
    private static final String TAG = "PhotoTransactionBuilder";

    public static void get(Context context, String objectName, String url, boolean getCircle, boolean isSync) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET/ProfilePhotoDownload")
                    .key((isSync ? "Sync/" : "") + objectName + ":" + url)
                    .priority(Priority.LOW)
                    .listener(PhotoTransactionListener.class)
                    .listenerParams(PhotoTransactionListener.pGet(url, getCircle))
                    .isSyncCall(isSync)
                    .request(
                            new HttpJsonBuilder()
                                    .method("GET")
                                    .path(url)
                    ).build();
            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

    }
}
