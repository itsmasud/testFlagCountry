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

    public static void getDirections(Context context, long workorderId, Position... positions) {
        Log.v(TAG, "getDirections");
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
                    .urlParams("?access_token=" + context.getString(R.string.mapbox_accessToken));

            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(GmapsTransactionHandler.class)
                    .handlerParams(GmapsTransactionHandler.pDirections(workorderId))
                    .useAuth(false)
                    .isSyncCall(false)
                    .request(builder)
                    .send();

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    // https://api.mapbox.com/v4/mapbox.dark/url-https%3A%2F%2Fmapbox.com%2Fimg%2Frocket.png(-76.9,38.9)/-76.9,38.9,15/1000x1000.png
    public static void getStaticMapClassic(Context context, long workorderId, int width, int height, Marker start, Marker end) {
        Log.v(TAG, "getStaticMapClassic");
        try {
            // https://api.mapbox.com/v4/jacobbeasley.ggg811om/
            // url-https%3A%2F%2Fmapbox.com%2Fimg%2Frocket.png(-76.9,38.9),
            // url-https%3A%2F%2Fmapbox.com%2Fimg%2Frocket.png(-76.9,38.5)
            // /auto/800x250.png?access_token=pk.eyJ1IjoiamFjb2JiZWFzbGV5IiwiYSI6IlBGelg4WDAifQ.KtpfrNfln0jplKdKO-xSZA

            String path = "/v4/mapbox.streets/";

            path += start.toString() + ",";
            path += end.toString();
            path += "/auto/" + width + "x" + height + ".png";

            HttpJsonBuilder builder = new HttpJsonBuilder()
                    .protocol("https")
                    .method("GET")
                    .timingKey("GET/mapbox/staticMapClassic")
                    .host("api.mapbox.com")
                    .path(path)
                    .urlParams("?access_token=" + context.getString(R.string.mapbox_accessToken));

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
