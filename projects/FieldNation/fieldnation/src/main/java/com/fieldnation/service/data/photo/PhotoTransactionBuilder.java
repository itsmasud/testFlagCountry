package com.fieldnation.service.data.photo;

import android.content.Context;

import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransactionBuilder;

/**
 * Created by Michael Carver on 4/22/2015.
 */
public class PhotoTransactionBuilder implements PhotoConstants {

    public static void get(Context context, String objectName, String url, boolean getCircle, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .key((isSync ? "Sync/" : "") + objectName + ":" + url)
                    .priority(Priority.LOW)
                    .handler(PhotoTransactionHandler.class)
                    .handlerParams(PhotoTransactionHandler.pGet(url, getCircle))
                    .isSyncCall(isSync)
                    .request(
                            new HttpJsonBuilder()
                                    .method("GET")
                                    .path(url)
                    ).send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
