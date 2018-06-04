package com.fieldnation.service.data.gmaps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.fieldnation.R;
import com.fieldnation.data.gmaps.GmapsDirections;
import com.fieldnation.fnhttpjson.HttpJsonBuilder;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Pigeon;
import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionSystem;

/**
 * Created by Shoaib on 10/14/2016.
 */
public abstract class GmapsClient extends Pigeon implements GmapsConstants {
    private static final String TAG = "GmapsClient";

    public static void getDirections(Context context, int workOrderId, Position... positions) {
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
//                    .listener(GmapsTransactionListener.class)
//                    .listenerParams(GmapsTransactionListener.pDirections(workorderId))
//                    .useAuth(false)
//                    .isSyncCall(false)
//                    .request(builder)
//                    .send();
//
//        } catch (Exception ex) {
//            Log.v(TAG, ex);
//        }
    }

    public void subDirections() {
        PigeonRoost.sub(this, ADDRESS_DIRECTIONS);
    }

    public void unsubDirections() {
        PigeonRoost.unsub(this, ADDRESS_DIRECTIONS);
    }

    // result: https://maps.googleapis.com/maps/api/staticmap?markers=color:red%7C%7C23.8758898,90.3841423&markers=color:red%7C%7C45.0815,-93.4059&size=432x180&maptype=roadmap&format=png&key=AIzaSyCeb9sNAn7lDagc6VKR6bb12g4CSZQRDj0
    public static void getStaticMapClassic(Context context, int workOrderId, Marker start, Marker end, int width, int height) {
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
                    .host("maps.googleapis.com")
                    .path(path)
                    .urlParams("?key=" + context.getString(R.string.gmap_apiKey));

            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET/gmap/staticMapClassic")
                    .priority(Priority.HIGH)
                    .listener(GmapsTransactionListener.class)
                    .listenerParams(GmapsTransactionListener.pStaticMapClassic(workOrderId))
                    .useAuth(false)
                    .setType(WebTransaction.Type.NORMAL)
                    .request(builder)
                    .build();

            WebTransactionSystem.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public void subStaticMapClassic() {
        PigeonRoost.sub(this, ADDRESS_STATIC_MAP_CLASSIC);
    }

    public void unsubStaticMapClassic() {
        PigeonRoost.unsub(this, ADDRESS_STATIC_MAP_CLASSIC);
    }

    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/

    @Override
    public void onMessage(String address, Object message) {
        Log.v(TAG, "onEvent: " + address);
        Bundle bundle = (Bundle) message;
        int workOrderId = bundle.getInt(PARAM_WORKORDER_ID);

        if (continueProcessing(workOrderId)) {
            if (address.startsWith(ADDRESS_DIRECTIONS)) {
                preOnDirections(bundle);
            } else if (address.startsWith(ADDRESS_STATIC_MAP_CLASSIC)) {
                preOnStaticMapClassic(bundle);
            }
        }
    }

    public abstract boolean continueProcessing(int workOrderId);

    private void preOnDirections(Bundle bundle) {
        Log.v(TAG, "preOnDirections");
        new AsyncTaskEx<Bundle, Object, GmapsDirections>() {
            private int _workOrderId;

            @Override
            protected GmapsDirections doInBackground(Bundle... params) {
                try {
                    _workOrderId = params[0].getInt(PARAM_WORKORDER_ID);
                    byte[] data = params[0].getByteArray(PARAM_DIRECTIONS);

                    return GmapsDirections.fromJson(new JsonObject(data));
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
                return null;
            }

            @Override
            protected void onPostExecute(GmapsDirections directions) {
                onDirections(_workOrderId, directions);
            }
        }.executeEx(bundle);
    }

    public void onDirections(int workOrderId, GmapsDirections directions) {
    }

    private void preOnStaticMapClassic(Bundle bundle) {
        new AsyncTaskEx<Bundle, Object, Bitmap>() {
            private int _workOrderId = 0;
            private boolean _failed = false;

            @Override
            protected Bitmap doInBackground(Bundle... params) {
                try {
                    Bundle bundle = params[0];
                    _workOrderId = bundle.getInt(PARAM_WORKORDER_ID);
                    _failed = bundle.containsKey(PARAM_FAILED) && bundle.getBoolean(PARAM_FAILED);

                    if (bundle.containsKey(PARAM_IMAGE_DATA)) {
                        byte[] imageData = bundle.getByteArray(PARAM_IMAGE_DATA);
                        return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                    } else {
                        return null;
                    }
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                onStaticMapClassic(_workOrderId, bitmap, _failed);
            }
        }.executeEx(bundle);
    }

    public void onStaticMapClassic(int workOrderId, Bitmap bitmap, boolean failed) {
    }
}
