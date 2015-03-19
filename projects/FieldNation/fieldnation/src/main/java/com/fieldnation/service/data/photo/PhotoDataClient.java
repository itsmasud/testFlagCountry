package com.fieldnation.service.data.photo;

import android.content.Context;
import android.content.Intent;

import com.fieldnation.UniqueTag;
import com.fieldnation.service.topics.TopicClient;

import java.io.File;

/**
 * Created by Michael Carver on 3/12/2015.
 */
public class PhotoDataClient implements PhotoConstants {
    private String TAG = UniqueTag.makeTag("service.data.PhotoDataClient");

    private TopicClient _client = null;
    private Listener _listener;

    public PhotoDataClient(Context context, Listener listener) {
        _listener = listener;
//        _client = new TopicClient(_clientListener);
//        _client.connect(context);
    }

    /**
     * Disconnects from the service. Call this in your activities onPause, or similar.
     *
     * @param context
     */
    public void stop(Context context) {
        _client.disconnect(context);
    }

    public void getPhoto(Context context, String url, boolean getCircle) {
        Intent intent = new Intent(context, PhotoDataService.class);
        intent.putExtra(PARAM_CIRCLE, getCircle);
        intent.putExtra(PARAM_URL, url);

        context.startService(intent);
    }

/*
    private final TopicClient.Listener _clientListener = new TopicClient.Listener() {
        @Override
        public void onConnected() {
            _client.register(TOPIC_ID_PHOTO_READY, TAG);
        }

        @Override
        public void onDisconnected() {
        }

        @Override
        public void onRegistered(String topicId) {
        }

        @Override
        public void onEvent(String topicId, Bundle payload) {
            if (topicId.equals(TOPIC_ID_PHOTO_READY)) {
                _listener.onPhoto(payload.getString(PARAM_URL), (File) payload.getSerializable(RESULT_IMAGE_FILE), payload.getBoolean(PARAM_CIRCLE));
            }
        }
    };
*/

    public interface Listener {
        public void onPhoto(String url, File file, boolean isCircle);
    }
}
