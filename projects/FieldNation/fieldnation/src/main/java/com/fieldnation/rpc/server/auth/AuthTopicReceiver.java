package com.fieldnation.rpc.server.auth;

import android.os.Bundle;
import android.os.Handler;

import com.fieldnation.topics.TopicReceiver;

/**
 * Created by Michael on 12/15/2014.
 */
public abstract class AuthTopicReceiver extends TopicReceiver {

    private String lastauthtoken = null;

    public AuthTopicReceiver(Handler handler) {
        super(handler);
    }

    @Override
    public void onTopic(int resultCode, String topicId, Bundle parcel) {
        String type = parcel.getString(AuthTopicService.BUNDLE_PARAM_TYPE);
        if (AuthTopicService.BUNDLE_PARAM_TYPE_COMPLETE.equals(type)) {
            OAuth auth = parcel.getParcelable(AuthTopicService.BUNDLE_PARAM_AUTH_TOKEN);
            onAuthentication(auth, !auth.getAccessToken().equals(lastauthtoken));
            lastauthtoken = auth.getAccessToken();
        } else if (AuthTopicService.BUNDLE_PARAM_TYPE_INVALID.equals(type)) {
        } else if (AuthTopicService.BUNDLE_PARAM_TYPE_FAILED.equals(type)) {
        } else if (AuthTopicService.BUNDLE_PARAM_TYPE_NO_NETWORK.equals(type)) {
        }
    }

    public abstract void onAuthentication(OAuth auth, boolean isNew);
}
