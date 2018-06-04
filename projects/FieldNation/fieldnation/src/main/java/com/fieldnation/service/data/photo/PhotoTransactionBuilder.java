package com.fieldnation.service.data.photo;

import android.content.Context;

import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionSystem;

/**
 * Created by Michael Carver on 4/22/2015.
 */
public class PhotoTransactionBuilder implements PhotoConstants {
    private static final String TAG = "PhotoTransactionBuilder";

    public static void get(Context context, String objectName, String sourceUrl, boolean getCircle, boolean isSync) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET/ProfilePhotoDownload")
                    .key((isSync ? "Sync/" : "") + objectName + ":" + sourceUrl)
                    .priority(Priority.HIGH)
                    .listener(PhotoTransactionListener.class)
                    .listenerParams(PhotoTransactionListener.pGet(sourceUrl, getCircle))
                    .setType(isSync ? WebTransaction.Type.CRAWLER : WebTransaction.Type.NORMAL)
                    .request(
                            new HttpJsonBuilder()
                                    .method("GET")
                                    .path(sourceUrl)
                    ).build();
            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }
}
