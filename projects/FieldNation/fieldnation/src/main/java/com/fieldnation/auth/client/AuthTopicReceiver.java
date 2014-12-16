package com.fieldnation.auth.client;

import android.os.Bundle;
import android.os.Handler;

import com.fieldnation.topics.TopicReceiver;

/**
 * Created by Michael on 12/15/2014.
 */
public abstract class AuthTopicReceiver extends TopicReceiver {

    public AuthTopicReceiver(Handler handler) {
        super(handler);
    }

    @Override
    public void onUnregister(int resultCode, String topicId) {
    }

    @Override
    public void onTopic(int resultCode, String topicId, Bundle parcel) {
        String type = parcel.getString(AuthTopicService.BUNDLE_PARAM_TYPE);
        if (AuthTopicService.BUNDLE_PARAM_TYPE_COMPLETE.equals(type)) {
            onAuthentication(parcel.getString(AuthTopicService.BUNDLE_PARAM_USERNAME),
                    parcel.getString(AuthTopicService.BUNDLE_PARAM_AUTH_TOKEN));
        } else if (AuthTopicService.BUNDLE_PARAM_TYPE_INVALID.equals(type)) {
            onAuthenticationInvalidated();
        } else if (AuthTopicService.BUNDLE_PARAM_TYPE_FAILED.equals(type)) {
            onAuthenticationFailed();
        }
    }

    public abstract void onAuthentication(String username, String authToken);

    public abstract void onAuthenticationFailed();

    public abstract void onAuthenticationInvalidated();
}
