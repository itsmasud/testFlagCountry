package com.fieldnation.service.data.photo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.UniqueTag;
import com.fieldnation.service.topics.TopicClient;

import java.io.File;

/**
 * Created by Michael Carver on 3/12/2015.
 */
public class PhotoClient extends TopicClient implements PhotoConstants {
    private final String TAG = UniqueTag.makeTag("PhotoDataClient");

    public PhotoClient(Listener listener) {
        super(listener);
    }

    public void disconnect(Context context) {
        super.disconnect(context, TAG);
    }

    public static void get(Context context, String url, boolean getCircle, boolean isSync) {
        Intent intent = new Intent(context, PhotoService.class);
        intent.putExtra(PARAM_ACTION, PARAM_ACTION_GET);
        intent.putExtra(PARAM_CIRCLE, getCircle);
        intent.putExtra(PARAM_URL, url);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public boolean subGet(String url, boolean getCircle, boolean isSync) {
        String topicId = TOPIC_ID_GET_PHOTO;

        if (isSync) {
            topicId += "_SYNC";
        }

        if (getCircle) {
            topicId += "/Circle";
        }

        topicId += url;

        return register(topicId, TAG);
    }

    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            Bundle bundle = (Bundle) payload;
            String action = bundle.getString(PARAM_ACTION);

            if (action.startsWith(PARAM_ACTION_GET))
                if (bundle.containsKey(PARAM_ERROR) && bundle.getBoolean(PARAM_ERROR)) {
                    onGet(bundle.getString(PARAM_URL),
                            null,
                            bundle.getBoolean(PARAM_CIRCLE), true);
                } else {
                    onGet(bundle.getString(PARAM_URL),
                            (File) bundle.getSerializable(RESULT_IMAGE_FILE),
                            bundle.getBoolean(PARAM_CIRCLE), false);
                }
        }


        public void onGet(String url, File file, boolean isCircle, boolean failed) {
        }
    }
}
