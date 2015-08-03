package com.fieldnation.topics;

import android.os.Bundle;
import android.os.Handler;

/**
 * Created by michael.carver on 12/23/2014.
 */
abstract class FileUploadTopicReceiver extends TopicReceiver {

    public FileUploadTopicReceiver(Handler handler) {
        super(handler);
    }

    @Override
    public void onTopic(int resultCode, String topicId, Bundle parcel) {
        String state = parcel.getString(Topics.TOPIC_FILE_UPLOAD_PARAM_STATE);
        if (Topics.TOPIC_FILE_UPLOAD_PARAM_STATE_START.equals(state)) {
            onStart(parcel.getString(Topics.TOPIC_FILE_UPLOAD_PARAM_URL),
                    parcel.getString(Topics.TOPIC_FILE_UPLOAD_PARAM_FILENAME));

        } else if (Topics.TOPIC_FILE_UPLOAD_PARAM_STATE_FINISH.equals(state)) {
            onFinish(parcel.getString(Topics.TOPIC_FILE_UPLOAD_PARAM_URL),
                    parcel.getString(Topics.TOPIC_FILE_UPLOAD_PARAM_FILENAME));

        } else if (Topics.TOPIC_FILE_UPLOAD_PARAM_STATE_ERROR.equals(state)) {
            onError(parcel.getString(Topics.TOPIC_FILE_UPLOAD_PARAM_URL),
                    parcel.getString(Topics.TOPIC_FILE_UPLOAD_PARAM_FILENAME),
                    parcel.getString(Topics.TOPIC_FILE_UPLOAD_PARAM_ERROR_MESSAGE));
        }
    }

    public abstract void onStart(String url, String filename);

    public abstract void onFinish(String url, String filename);

    public abstract void onError(String url, String filename, String message);
}
