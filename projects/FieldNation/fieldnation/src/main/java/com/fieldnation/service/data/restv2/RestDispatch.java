package com.fieldnation.service.data.restv2;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.service.topics.TopicService;

/**
 * Created by Michael Carver on 4/30/2015.
 */
public class RestDispatch implements RestConstants {

    public static void object(Context context, String resultTag, String objectType, String id, Bundle object, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_RESULT_TAG, resultTag);
        bundle.putString(PARAM_OBJECT_TYPE, objectType);
        bundle.putString(PARAM_OBJECT_ID, id);
        bundle.putBundle(PARAM_OBJECT_DATA_BUNDLE, object);
        bundle.putBoolean(PARAM_SYNC, isSync);

        String topicId = TOPIC_OBJECT;

        if (isSync) {
            topicId += "_SYNC";
        }

        if (resultTag != null) {
            topicId += "/" + resultTag;
        }

        if (objectType != null) {
            topicId += "/" + objectType;

            if (id != null) {
                topicId += "/" + id;
            }
        }

        TopicService.dispatchEvent(context, topicId, bundle, true);
    }
}
