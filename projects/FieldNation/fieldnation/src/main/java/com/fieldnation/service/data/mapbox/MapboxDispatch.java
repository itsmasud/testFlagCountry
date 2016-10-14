package com.fieldnation.service.data.mapbox;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fnpigeon.TopicService;

/**
 * Created by Michael on 6/22/2016.
 */
public class MapboxDispatch implements MapboxConstants {
    private static final String TAG = "GmapsDispatch";

    public static void directions(Context context, long workorderId, byte[] directions) {
        Log.v(TAG, "directions");
        Bundle bundle = new Bundle();
        bundle.putByteArray(PARAM_DIRECTIONS, directions);
        bundle.putLong(PARAM_WORKORDER_ID, workorderId);

        TopicService.dispatchEvent(context, TOPIC_ID_DIRECTIONS + "/" + workorderId, bundle, Sticky.TEMP);
    }

    public static void staticMapClassic(Context context, long workorderId, byte[] imageData, boolean failed) {
        Bundle bundle = new Bundle();

        if (imageData != null)
            bundle.putByteArray(PARAM_IMAGE_DATA, imageData);
        if (failed)
            bundle.putBoolean(PARAM_FAILED, failed);
        bundle.putLong(PARAM_WORKORDER_ID, workorderId);

        TopicService.dispatchEvent(context, TOPIC_ID_STATIC_MAP_CLASSIC + "/" + workorderId, bundle, Sticky.TEMP);
    }
}
