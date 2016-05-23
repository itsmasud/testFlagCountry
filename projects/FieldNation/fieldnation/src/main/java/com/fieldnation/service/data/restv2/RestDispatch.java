package com.fieldnation.service.data.restv2;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.json.JsonObject;
import com.fieldnation.service.topics.Sticky;
import com.fieldnation.service.topics.TopicService;

/**
 * Created by Michael Carver on 4/30/2015.
 */
public class RestDispatch implements RestConstants {

    public static void object(Context context, String resultTag, String objectType, String id, Bundle object, Sticky sticky, boolean isSync) {
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

        TopicService.dispatchEvent(context, topicId, bundle, sticky);
    }

    public static void object(Context context, String resultTag, String objectType, String id,
                              Parcelable object, Sticky sticky, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_RESULT_TAG, resultTag);
        bundle.putString(PARAM_OBJECT_TYPE, objectType);
        bundle.putString(PARAM_OBJECT_ID, id);
        bundle.putParcelable(PARAM_OBJECT_DATA_PARCELABLE, object);
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

        TopicService.dispatchEvent(context, topicId, bundle, sticky);
    }

    public static void list(Context context, String resultTag, String objectType,
                            JsonObject envelope, Sticky sticky, boolean isSync) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_RESULT_TAG, resultTag);
        bundle.putString(PARAM_OBJECT_TYPE, objectType);
        bundle.putParcelable(PARAM_OBJECT_DATA_PARCELABLE, envelope);
        bundle.putBoolean(PARAM_SYNC, isSync);

        String topicId = TOPIC_OBJECT + "_LIST";

        if (isSync) {
            topicId += "_SYNC";
        }

        if (resultTag != null) {
            topicId += "/" + resultTag;
        }

        if (objectType != null) {
            topicId += "/" + objectType;
        }

        TopicService.dispatchEvent(context, topicId, bundle, sticky);
    }
}
