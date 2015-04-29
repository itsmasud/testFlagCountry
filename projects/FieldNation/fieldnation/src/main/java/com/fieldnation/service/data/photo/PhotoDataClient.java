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
public class PhotoDataClient extends TopicClient implements PhotoConstants {
    private String TAG = UniqueTag.makeTag("PhotoDataClient");

    public PhotoDataClient(Listener listener) {
        super(listener);
    }

    public void disconnect(Context context) {
        super.disconnect(context, TAG);
    }

    public static void dispatchGetPhoto(Context context, String url, boolean getCircle, boolean isSync) {
        Intent intent = new Intent(context, PhotoDataService.class);
        intent.putExtra(PARAM_CIRCLE, getCircle);
        intent.putExtra(PARAM_URL, url);
        intent.putExtra(PARAM_IS_SYNC, isSync);
        context.startService(intent);
    }

    public boolean getPhoto(Context context, String url, boolean getCircle) {
        dispatchGetPhoto(context, url, getCircle, false);
        return register(TOPIC_ID_PHOTO_READY
                + (getCircle ? "/Circle" : "")
                + "/" + url, TAG);
    }

    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            Bundle bundle = (Bundle) payload;
            onPhoto(bundle.getString(PARAM_URL),
                    (File) bundle.getSerializable(RESULT_IMAGE_FILE),
                    bundle.getBoolean(PARAM_CIRCLE));
        }


        public void onPhoto(String url, File file, boolean isCircle) {
        }
    }
}
