package com.fieldnation.service.data.v2.profile;

import android.content.Context;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionService;

/**
 * Created by Michael on 9/12/2016.
 */
public class ProfileTransactionBuilder {
    private static final String TAG = "ProfileTransactionBuilders";

    public static void geo(Context context, double lat, double lon) {
        try {
            JsonObject body = new JsonObject();
            body.put("lat", lat);
            body.put("lon", lon);

            HttpJsonBuilder http = new HttpJsonBuilder()
                    .path("/api/rest/v2/profile/geo")
                    .protocol("https")
                    .body(body.toString())
                    .method("POST");

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET/api/rest/v2/profile/geo")
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(http)
                    .build();

            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

}