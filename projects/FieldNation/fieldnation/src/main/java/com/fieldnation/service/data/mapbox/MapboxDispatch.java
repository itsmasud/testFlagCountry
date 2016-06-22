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
}
