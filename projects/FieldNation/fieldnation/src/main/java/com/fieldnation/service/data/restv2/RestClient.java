package com.fieldnation.service.data.restv2;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.fieldnation.UniqueTag;
import com.fieldnation.service.topics.TopicClient;

import java.util.Map;

/**
 * Created by Michael Carver on 4/30/2015.
 */
public class RestClient extends TopicClient implements RestConstants {
    private static final String STAG = "RestClient";
    private final String TAG = UniqueTag.makeTag(STAG);

    public RestClient(Listener listener) {
        super(listener);
    }

    // requests
    public static void list(Context context, String resultTag, String objectType, Map<String, String> params, boolean isSync) {
        Intent intent = new Intent(context, RestService.class);
        intent.putExtra(PARAM_ACTION, TOPIC_LIST);
        intent.putExtra(PARAM_RESULT_TAG, resultTag);
        intent.putExtra(PARAM_OBJECT_TYPE, PARAM_OBJECT_TYPE);
        intent.putExtra(PARAM_SYNC, isSync);
        context.startService(intent);
    }

    public static void create(Context context, String resultTag, String objectType, byte[] obj) {

    }

    public static void get(Context context, String resultTag, String objectType, String id, boolean isSync) {

    }

    public static void update(Context context, String resultTag, String objectType, String id, byte[] body, boolean isSync) {

    }


    public static void delete(Context context, String resultTag, String objectType, String id, boolean isSync) {

    }

    public static void action(Context context, String resultTag, String objectType, String id, String action, byte[] body, boolean isSync) {

    }

    // subscriptions
    public boolean subObject(String resultTag, String objectType, String id, boolean isSync) {
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

        return register(topicId, TAG);
    }

    public boolean subList(String resultTag, String objectType, String id, boolean isSync) {
        String topicId = TOPIC_LIST;

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

        // result could look like one fo the following
        // RestConstants:TOPIC_LIST_SYNC/resultTag/objectType/id
        return register(topicId, TAG);
    }

    public boolean subCreate(String resultTag, String objectType) {
        String topicId = TOPIC_CREATE;

        if (resultTag != null) {
            topicId += "/" + resultTag;
        }

        if (objectType != null) {
            topicId += "/" + objectType;
        }

        return register(topicId, TAG);
    }

    public boolean subGet(String resultTag, String objectType, String id, boolean isSync) {
        String topicId = TOPIC_GET;

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

        return register(topicId, TAG);
    }

    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {

        }
    }
}
