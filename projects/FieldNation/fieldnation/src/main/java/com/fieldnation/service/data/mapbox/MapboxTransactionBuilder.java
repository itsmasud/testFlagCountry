package com.fieldnation.service.data.mapbox;

import android.content.Context;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransactionBuilder;

/**
 * Created by Michael on 6/22/2016.
 */
public class MapboxTransactionBuilder implements MapboxConstants {
    private static final String TAG = "MapboxTransactionBuilder";

    public static void getDirections(Context context, Position[] positions) {
        try {
            String path = "/directions/v5/mapbox/driving/";
            for (int i = 0; i < positions.length; i++) {
                path += positions[i];
                if (i < positions.length - 1)
                    path += ";";
            }

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .timingKey("GET/mapbox/directions")
                    .host("api.mapbox.com")
                    .path(path)
                    .urlParams("access_token=" + context.getString(R.string.mapbox_accessToken));

            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(MapboxTransactionHandler.class)
                    .handlerParams(MapboxTransactionHandler.pDirections())
                    .useAuth(false)
                    .isSyncCall(false)
                    .request(builder)
                    .send();

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }
}
