package com.fieldnation.topics;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;

/**
 * Created by michael.carver on 12/12/2014.
 */
public abstract class TopicReceiver extends ResultReceiver implements TopicConstants {
    public TopicReceiver() {
        super(null);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        String action = resultData.getString(ACTION);
        if (ACTION_REGISTER_LISTENER.equals(action)) {
            onRegister(resultCode, resultData.getString(PARAM_TOPIC_ID));
        } else if (ACTION_UNREGISTER_LISTENER.equals(action)) {
            onUnregister(resultCode, resultData.getString(PARAM_TOPIC_ID));
        } else if (ACTION_DISPATCH_EVENT.equals(action)) {
            onTopic(resultCode, resultData.getString(PARAM_TOPIC_ID), resultData.getBundle(PARAM_TOPIC_PARCEL));
        } else if (ACTION_DELETE_CLIENT.equals(action)) {
            onDelete(resultCode, resultData.getString(PARAM_TOPIC_ID));
        }
    }

    public abstract void onRegister(int resultCode, String topicId);

    public abstract void onUnregister(int resultCode, String topicId);

    public abstract void onTopic(int resultCode, String topicId, Bundle parcel);

    public abstract void onDelete(int resultCode, String topicId);
}
