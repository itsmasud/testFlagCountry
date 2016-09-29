package com.fieldnation.service.data.v2.profile;

import android.content.Context;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransactionBuilder;

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
                    .timingKey("GET/api/rest/v2/profile/geo")
                    .path("/api/rest/v2/profile/geo")
                    .protocol("https")
                    .body(body.toString())
                    .method("POST");

            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .useAuth(true)
                    .request(http)
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

}