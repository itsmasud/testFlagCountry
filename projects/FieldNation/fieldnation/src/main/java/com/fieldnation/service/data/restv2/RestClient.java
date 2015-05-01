package com.fieldnation.service.data.restv2;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.fieldnation.UniqueTag;
import com.fieldnation.service.topics.TopicClient;

import java.io.File;

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
    public static void list(Context context, String resultTag, String objectType, String params, boolean isSync) {
        Intent intent = new Intent(context, RestService.class);
        intent.putExtra(PARAM_TOPIC, TOPIC_LIST);
        intent.putExtra(PARAM_RESULT_TAG, resultTag);
        intent.putExtra(PARAM_OBJECT_TYPE, objectType);
        intent.putExtra(PARAM_URL_PARAMS, params);
        intent.putExtra(PARAM_SYNC, isSync);
        context.startService(intent);
    }

    // TODO, I don't like this, wont handle files, or large data, doesn't handle different content types
    public static void create(Context context, String resultTag, String objectType, String contentType, byte[] obj) {
        Intent intent = new Intent(context, RestService.class);
        intent.putExtra(PARAM_TOPIC, TOPIC_CREATE);
        intent.putExtra(PARAM_RESULT_TAG, resultTag);
        intent.putExtra(PARAM_OBJECT_TYPE, objectType);
        intent.putExtra(PARAM_CONTENT_TYPE, contentType);
        intent.putExtra(PARAM_OBJECT_DATA_BYTE_ARRAY, obj);
        context.startService(intent);
    }

    public static void create(Context context, String resultTag, String objectType, String contentType, String obj) {
        Intent intent = new Intent(context, RestService.class);
        intent.putExtra(PARAM_TOPIC, TOPIC_CREATE);
        intent.putExtra(PARAM_RESULT_TAG, resultTag);
        intent.putExtra(PARAM_OBJECT_TYPE, objectType);
        intent.putExtra(PARAM_CONTENT_TYPE, contentType);
        intent.putExtra(PARAM_OBJECT_DATA_STRING, obj);
        context.startService(intent);
    }

    public static void create(Context context, String resultTag, String objectType, File file) {
        Intent intent = new Intent(context, RestService.class);
        intent.putExtra(PARAM_TOPIC, TOPIC_CREATE);
        intent.putExtra(PARAM_RESULT_TAG, resultTag);
        intent.putExtra(PARAM_OBJECT_TYPE, objectType);
        intent.putExtra(PARAM_OBJECT_DATA_FILE, file);
        context.startService(intent);
    }

    public static void get(Context context, String resultTag, String objectType, String id, boolean isSync) {
        Intent intent = new Intent(context, RestService.class);
        intent.putExtra(PARAM_TOPIC, TOPIC_GET);
        intent.putExtra(PARAM_RESULT_TAG, resultTag);
        intent.putExtra(PARAM_OBJECT_TYPE, objectType);
        intent.putExtra(PARAM_OBJECT_ID, id);
        intent.putExtra(PARAM_SYNC, isSync);
        context.startService(intent);
    }

    public static void update(Context context, String resultTag, String objectType, String id, String contentType, byte[] body, boolean isSync) {
        Intent intent = new Intent(context, RestService.class);
        intent.putExtra(PARAM_TOPIC, TOPIC_GET);
        intent.putExtra(PARAM_RESULT_TAG, resultTag);
        intent.putExtra(PARAM_OBJECT_TYPE, objectType);
        intent.putExtra(PARAM_OBJECT_ID, id);
        intent.putExtra(PARAM_CONTENT_TYPE, contentType);
        intent.putExtra(PARAM_OBJECT_DATA_BYTE_ARRAY, body);
        intent.putExtra(PARAM_SYNC, isSync);
        context.startService(intent);
    }

    public static void update(Context context, String resultTag, String objectType, String id, String contentType, String body, boolean isSync) {
        Intent intent = new Intent(context, RestService.class);
        intent.putExtra(PARAM_TOPIC, TOPIC_GET);
        intent.putExtra(PARAM_RESULT_TAG, resultTag);
        intent.putExtra(PARAM_OBJECT_TYPE, objectType);
        intent.putExtra(PARAM_OBJECT_ID, id);
        intent.putExtra(PARAM_CONTENT_TYPE, contentType);
        intent.putExtra(PARAM_OBJECT_DATA_STRING, body);
        intent.putExtra(PARAM_SYNC, isSync);
        context.startService(intent);
    }

    public static void update(Context context, String resultTag, String objectType, String id, File file, boolean isSync) {
        Intent intent = new Intent(context, RestService.class);
        intent.putExtra(PARAM_TOPIC, TOPIC_GET);
        intent.putExtra(PARAM_RESULT_TAG, resultTag);
        intent.putExtra(PARAM_OBJECT_TYPE, objectType);
        intent.putExtra(PARAM_OBJECT_ID, id);
        intent.putExtra(PARAM_OBJECT_DATA_FILE, file);
        intent.putExtra(PARAM_SYNC, isSync);
        context.startService(intent);
    }

    public static void delete(Context context, String resultTag, String objectType, String id, boolean isSync) {
        Intent intent = new Intent(context, RestService.class);
        intent.putExtra(PARAM_TOPIC, TOPIC_DELETE);
        intent.putExtra(PARAM_RESULT_TAG, resultTag);
        intent.putExtra(PARAM_OBJECT_TYPE, objectType);
        intent.putExtra(PARAM_OBJECT_ID, id);
        intent.putExtra(PARAM_SYNC, isSync);
        context.startService(intent);
    }

    public static void action(Context context, String resultTag, String objectType, String id, String action,
                              String urlParams, String contentType, String body, boolean isSync) {
        Intent intent = new Intent(context, RestService.class);
        intent.putExtra(PARAM_TOPIC, TOPIC_ACTION);
        intent.putExtra(PARAM_RESULT_TAG, resultTag);
        intent.putExtra(PARAM_OBJECT_TYPE, objectType);
        intent.putExtra(PARAM_OBJECT_ID, id);
        intent.putExtra(PARAM_ACTION, action);
        intent.putExtra(PARAM_URL_PARAMS, urlParams);
        intent.putExtra(PARAM_CONTENT_TYPE, contentType);
        intent.putExtra(PARAM_OBJECT_DATA_STRING, body);
        intent.putExtra(PARAM_SYNC, isSync);
        context.startService(intent);
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

    public boolean subList(String resultTag, String objectType, boolean isSync) {
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

        return register(topicId, TAG);
    }

    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {

        }
    }
}
