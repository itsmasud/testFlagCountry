package com.fieldnation.topics;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by michael.carver on 12/12/2014.
 */
abstract class TopicReceiver extends ResultReceiver implements TopicConstants {
    public TopicReceiver(Handler handler) {
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        String action = resultData.getString(ACTION);
        if (ACTION_REGISTER_LISTENER.equals(action)) {
            onRegister(resultCode, resultData.getString(PARAM_TOPIC_ID));
        } else if (ACTION_DISPATCH_EVENT.equals(action)) {
            onTopic(resultCode, resultData.getString(PARAM_TOPIC_ID), resultData.getBundle(PARAM_TOPIC_PARCEL));
        }
    }

    public void onRegister(int resultCode, String topicId) {
    }

    public abstract void onTopic(int resultCode, String topicId, Bundle parcel);
}
