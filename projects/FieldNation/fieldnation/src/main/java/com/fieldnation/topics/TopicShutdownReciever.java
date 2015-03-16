package com.fieldnation.topics;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by michael.carver on 12/18/2014.
 */
class TopicShutdownReciever extends TopicReceiver {
    private Activity activity;
    private String tag;

    public TopicShutdownReciever(Activity activity, Handler handler, String tag) {
        super(handler);
        this.activity = activity;
        this.tag = tag;
        TopicService.registerListener(activity, 0, tag, Topics.TOPIC_SHUTDOWN, this);
    }

    @Override
    public void onRegister(int resultCode, String topicId) {
    }

    @Override
    public void onTopic(int resultCode, String topicId, Bundle parcel) {
        if (Topics.TOPIC_SHUTDOWN.equals(topicId))
            activity.finish();
    }

    public void onPause() {
        TopicService.delete(activity, tag);
    }
}
