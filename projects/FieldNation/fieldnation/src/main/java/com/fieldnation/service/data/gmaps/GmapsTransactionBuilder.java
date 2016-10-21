package com.fieldnation.service.data.gmaps;

import android.content.Context;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransactionBuilder;

/**
 * Created by Shoaib on 6/22/2016.
 */
public class GmapsTransactionBuilder implements GmapsConstants {
    private static final String TAG = "GmapsTransactionBuilder";

//    public static void getDirections(Context context, long workorderId, Position... positions) {
//        Log.v(TAG, "getDirections");
//        try {
//            String path = "/directions/v5/mapbox/driving/";
//            for (int i = 0; i < positions.length; i++) {
//                path += positions[i];
//                if (i < positions.length - 1)
//                    path += ";";
//            }
//
//            HttpJsonBuilder builder = new HttpJsonBuilder()
//                    .protocol("https")
//                    .method("GET")
//                    .timingKey("GET/mapbox/directions")
//                    .host("api.mapbox.com")
//                    .path(path)
//                    .urlParams("?access_token=" + context.getString(R.string.mapbox_accessToken));
//
//            WebTransactionBuilder.builder(context)
//                    .priority(Priority.HIGH)
//                    .handler(GmapsTransactionHandler.class)
//                    .handlerParams(GmapsTransactionHandler.pDirections(workorderId))
//                    .useAuth(false)
//                    .isSyncCall(false)
//                    .request(builder)
//                    .send();
//
//        } catch (Exception ex) {
//            Log.v(TAG, ex);
//        }
//    }

    public static void getStaticMapClassic(Context context, long workorderId, int width, int height, Marker start, Marker end) {
        Log.v(TAG, "getStaticMapClassic");
        try {

            String path = "/maps/api/staticmap?";

            path += start.toString() + "&";
            path += end.toString();
            path += "&size=" + width + "x" + height;
            path += "&maptype=roadmap";
            path += "&format=png";

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .timingKey("GET/gmap/staticMapClassic")
                    .host("maps.googleapis.com")
                    .path(path)
                    .urlParams("?key=" + context.getString(R.string.gmap_apiKey));

            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(GmapsTransactionHandler.class)
                    .handlerParams(GmapsTransactionHandler.pStaticMapClassic(workorderId))
                    .useAuth(false)
                    .isSyncCall(false)
                    .request(builder)
                    .send();
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

}
