package com.fieldnation.service.activityresult;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.UniqueTag;
import com.fieldnation.service.topics.Sticky;
import com.fieldnation.service.topics.TopicClient;
import com.fieldnation.service.topics.TopicService;

/**
 * Created by Michael on 7/8/2016.
 */
public class ActivityResultClient extends TopicClient implements ActivityResultConstants {
    private static final String STAG = "ActivityResultClient";
    private final String TAG = UniqueTag.makeTag(STAG);

    public ActivityResultClient(Listener listener) {
        super(listener);
    }

    public void disconnect(Context context) {
        super.disconnect(context, TAG);
    }

    public static void startActivity(Context context, Intent intent) {
        TopicService.dispatchEvent(context, TOPIC_ID_START_ACTIVITY, intent, Sticky.NONE);
    }

    public boolean subStartActivity() {
        return register(TOPIC_ID_START_ACTIVITY, TAG);
    }

    public static void startActivityForResult(Context context, Intent intent, int requestCode) {
        Bundle payload = new Bundle();
        payload.putParcelable(PARAM_INTENT, intent);
        payload.putInt(PARAM_REQUEST_CODE, requestCode);

        TopicService.dispatchEvent(context, TOPIC_ID_START_ACTIVITY_FOR_RESULT, payload, Sticky.NONE);
    }

    public void clearActivityForResult(int requestCode) {
        clearTopic(TOPIC_ID_START_ACTIVITY_FOR_RESULT + "/" + requestCode);
    }

    public boolean subStartActivityForResult() {
        return register(TOPIC_ID_ON_ACTIVITY_RESULT, TAG);
    }

    public static void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        Bundle payload = new Bundle();
        payload.putInt(PARAM_REQUEST_CODE, requestCode);
        payload.putInt(PARAM_RESULT_CODE, resultCode);
        payload.putParcelable(PARAM_INTENT, data);

        TopicService.dispatchEvent(context, TOPIC_ID_ON_ACTIVITY_RESULT + "/" + requestCode, payload, Sticky.TEMP);
    }

    /**
     * Subscribe to this if you want to receive results from activities
     *
     * @return true if subscribed, false otherwise
     */
    public boolean subOnActivityResult() {
        return register(TOPIC_ID_ON_ACTIVITY_RESULT, TAG);
    }

    /**
     * Same as subOnActivityResult(), however, also subscribes to a specific resultCode
     *
     * @param requestCode
     * @return
     */
    public boolean subOnActivityResult(int requestCode) {
        return register(TOPIC_ID_ON_ACTIVITY_RESULT + "/" + requestCode, TAG);
    }

    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public static abstract class Listener extends TopicClient.Listener {
        @Override
        public void onEvent(String topicId, Parcelable payload) {
            if (topicId.startsWith(TOPIC_ID_ON_ACTIVITY_RESULT)) {
                preOnActivityResult((Bundle) payload);
            } else if (topicId.startsWith(TOPIC_ID_START_ACTIVITY_FOR_RESULT)) {
                preStartActivityForResult((Bundle) payload);
            } else if (topicId.startsWith(TOPIC_ID_START_ACTIVITY)) {
                startActivity((Intent) payload);
            }
        }

        /**
         * Override if you are an activity who will be making requests on behald of the rest of the app
         *
         * @param intent
         */
        public void startActivity(Intent intent) {
        }

        private void preStartActivityForResult(Bundle bundle) {
            startActivityForResult(
                    (Intent) bundle.getParcelable(PARAM_INTENT),
                    bundle.getInt(PARAM_REQUEST_CODE));
        }

        /**
         * Override if you are an activity who will be making requests on behalf of the rest of the app
         *
         * @param intent
         * @param requestCode
         */
        public void startActivityForResult(Intent intent, int requestCode) {
        }


        private void preOnActivityResult(Bundle bundle) {
            onActivityResult(
                    bundle.getInt(PARAM_REQUEST_CODE),
                    bundle.getInt(PARAM_RESULT_CODE),
                    (Intent) bundle.getParcelable(PARAM_INTENT));
        }

        /**
         * Override if you want to receive results from a startActivityForResult call
         *
         * @param requestCode
         * @param resultCode
         * @param data
         */
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
        }
    }
}
