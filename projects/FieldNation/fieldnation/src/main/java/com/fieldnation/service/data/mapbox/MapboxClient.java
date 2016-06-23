package com.fieldnation.service.data.mapbox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.Log;
import com.fieldnation.UniqueTag;
import com.fieldnation.data.mapbox.MapboxDirections;
import com.fieldnation.json.JsonObject;
import com.fieldnation.service.topics.TopicClient;

/**
 * Created by Michael on 6/22/2016.
 */
public class MapboxClient extends TopicClient implements MapboxConstants {
    private static final String STAG = "MapboxClient";
    private final String TAG = UniqueTag.makeTag(STAG);

    public MapboxClient(Listener listener) {
        super(listener);
    }

    public void disconnect(Context context) {
        super.disconnect(context, TAG);
    }

    public static void getDirections(Context context, Position start, Position end) {
        MapboxTransactionBuilder.getDirections(context, new Position[]{start, end});
    }

    public boolean subDirections() {
        return register(TOPIC_ID_DIRECTIONS, TAG);
    }

    public static void getStaticMapClassic(Context context, long workorderId, Marker start, Marker end, int width, int height) {
        MapboxTransactionBuilder.getStaticMapClassic(context, workorderId, width, height, start, end);
    }

    public boolean subStaticMapClassic(long workorderId) {
        return register(TOPIC_ID_STATIC_MAP_CLASSIC + "/" + workorderId, TAG);
    }

    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            switch (topicId) {
                case TOPIC_ID_DIRECTIONS:
                    preOnDirections((Bundle) payload);
                    break;
                case TOPIC_ID_STATIC_MAP_CLASSIC:
                    preOnStaticMapClassic((Bundle) payload);
                    break;
            }
        }

        private void preOnDirections(Bundle bundle) {
            new AsyncTaskEx<Bundle, Object, MapboxDirections>() {

                @Override
                protected MapboxDirections doInBackground(Bundle... params) {
                    try {
                        byte[] data = params[0].getByteArray(PARAM_DIRECTIONS);

                        return MapboxDirections.fromJson(new JsonObject(data));
                    } catch (Exception ex) {
                        Log.v(STAG, ex);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(MapboxDirections directions) {
                    onDirections(directions);
                }
            }.executeEx(bundle);
        }

        public void onDirections(MapboxDirections directions) {
        }

        private void preOnStaticMapClassic(Bundle bundle) {
            new AsyncTaskEx<Bundle, Object, Bitmap>() {
                private long _workorderId = 0;

                @Override
                protected Bitmap doInBackground(Bundle... params) {
                    Bundle bundle = params[0];
                    _workorderId = bundle.getLong(PARAM_WORKORDER_ID);
                    byte[] imageData = bundle.getByteArray(PARAM_IMAGE_DATA);
                    return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    onStaticMapClassic(_workorderId, bitmap);
                }
            }.executeEx(bundle);
        }

        public void onStaticMapClassic(long workorderId, Bitmap bitmap) {
        }
    }
}
