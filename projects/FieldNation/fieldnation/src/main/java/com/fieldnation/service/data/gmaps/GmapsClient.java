package com.fieldnation.service.data.gmaps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.data.gmaps.GmapsDirections;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.UniqueTag;

/**
 * Created by Shoaib on 10/14/2016.
 */
public class GmapsClient extends TopicClient implements GmapsConstants {
    private static final String STAG = "GmapsClient";
    private final String TAG = UniqueTag.makeTag(STAG);

    public GmapsClient(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    public static void getDirections(Context context, long workorderId, Position... positions) {
//        GmapsTransactionBuilder.getDirections(context, workorderId, positions);
    }

    public boolean subDirections(long workorderId) {
        Log.v(TAG, "subDirections");
        return register(TOPIC_ID_DIRECTIONS + "/" + workorderId);
    }

    public static void getStaticMapClassic(Context context, long workorderId, Marker start, Marker end, int width, int height) {
        GmapsTransactionBuilder.getStaticMapClassic(context, workorderId, width, height, start, end);
    }

    public boolean subStaticMapClassic(long workorderId) {
        return register(TOPIC_ID_STATIC_MAP_CLASSIC + "/" + workorderId);
    }

    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            Log.v(STAG, "onEvent: " + topicId);
            if (topicId.startsWith(TOPIC_ID_DIRECTIONS)) {
                preOnDirections((Bundle) payload);
            } else if (topicId.startsWith(TOPIC_ID_STATIC_MAP_CLASSIC)) {
                preOnStaticMapClassic((Bundle) payload);
            }
        }

        private void preOnDirections(Bundle bundle) {
            Log.v(STAG, "preOnDirections");
            new AsyncTaskEx<Bundle, Object, GmapsDirections>() {
                private long _workorderId;

                @Override
                protected GmapsDirections doInBackground(Bundle... params) {
                    try {
                        _workorderId = params[0].getLong(PARAM_WORKORDER_ID);
                        byte[] data = params[0].getByteArray(PARAM_DIRECTIONS);

                        return GmapsDirections.fromJson(new JsonObject(data));
                    } catch (Exception ex) {
                        Log.v(STAG, ex);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(GmapsDirections directions) {
                    onDirections(_workorderId, directions);
                }
            }.executeEx(bundle);
        }

        public void onDirections(long workorderId, GmapsDirections directions) {
        }

        private void preOnStaticMapClassic(Bundle bundle) {
            new AsyncTaskEx<Bundle, Object, Bitmap>() {
                private long _workorderId = 0;
                private boolean _failed = false;

                @Override
                protected Bitmap doInBackground(Bundle... params) {
                    try {
                        Bundle bundle = params[0];
                        _workorderId = bundle.getLong(PARAM_WORKORDER_ID);
                        _failed = bundle.containsKey(PARAM_FAILED) && bundle.getBoolean(PARAM_FAILED);

                        if (bundle.containsKey(PARAM_IMAGE_DATA)) {
                            byte[] imageData = bundle.getByteArray(PARAM_IMAGE_DATA);
                            return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                        } else {
                            return null;
                        }
                    } catch (Exception ex) {
                        Log.v(STAG, ex);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    onStaticMapClassic(_workorderId, bitmap, _failed);
                }
            }.executeEx(bundle);
        }

        public void onStaticMapClassic(long workorderId, Bitmap bitmap, boolean failed) {
        }
    }
}
