package com.fieldnation.service.data.mapbox;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.service.topics.Sticky;
import com.fieldnation.service.topics.TopicService;

/**
 * Created by Michael on 6/22/2016.
 */
public class MapboxDispatch implements MapboxConstants {

    public static void directions(Context context, byte[] directions) {
        Bundle bundle = new Bundle();
        bundle.putByteArray(PARAM_DIRECTIONS, directions);

        TopicService.dispatchEvent(context, TOPIC_ID_DIRECTIONS, bundle, Sticky.NONE);
    }

    public static void staticMapClassic(Context context, long workorderId, byte[] imageData, boolean failed) {
        Bundle bundle = new Bundle();

        if (imageData != null)
            bundle.putByteArray(PARAM_IMAGE_DATA, imageData);
        if (failed)
            bundle.putBoolean(PARAM_FAILED, failed);
        bundle.putLong(PARAM_WORKORDER_ID, workorderId);

        TopicService.dispatchEvent(context, TOPIC_ID_STATIC_MAP_CLASSIC + "/" + workorderId, bundle, Sticky.NONE);
    }
}
