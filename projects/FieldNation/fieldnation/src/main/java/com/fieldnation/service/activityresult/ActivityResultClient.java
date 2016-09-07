package com.fieldnation.service.activityresult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fnpigeon.TopicService;
import com.fieldnation.fntools.UniqueTag;

/**
 * Created by Michael on 7/8/2016.
 */
public class ActivityResultClient extends TopicClient implements ActivityResultConstants {
    private static final String STAG = "ActivityResultClient";
    private final String TAG = UniqueTag.makeTag(STAG);

    public ActivityResultClient(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    public static void startActivity(Context context, Intent intent) {
        Bundle payload = new Bundle();
        payload.putParcelable(PARAM_INTENT, intent);

        TopicService.dispatchEvent(context, TOPIC_ID_START_ACTIVITY, payload, Sticky.NONE);
    }

    public static void startActivity(Context context, Intent intent, int startAnimId, int endAnimId) {
        Bundle payload = new Bundle();
        payload.putParcelable(PARAM_INTENT, intent);
        payload.putInt(PARAM_TRANSITION_START_ANIMATION, startAnimId);
        payload.putInt(PARAM_TRANSITION_END_ANIMATION, endAnimId);

        TopicService.dispatchEvent(context, TOPIC_ID_START_ACTIVITY, payload, Sticky.NONE);
    }

    public boolean subStartActivity() {
        return register(TOPIC_ID_START_ACTIVITY);
    }

    // For Result stuff

    public static void startActivityForResult(Context context, Intent intent, int requestCode) {
        Bundle payload = new Bundle();
        payload.putParcelable(PARAM_INTENT, intent);
        payload.putInt(PARAM_REQUEST_CODE, requestCode);

        TopicService.dispatchEvent(context, TOPIC_ID_START_ACTIVITY_FOR_RESULT, payload, Sticky.NONE);
    }

    public static void startActivityForResult(Context context, Intent intent, int requestCode, int startAnimId, int endAnimId) {
        Bundle payload = new Bundle();
        payload.putParcelable(PARAM_INTENT, intent);
        payload.putInt(PARAM_REQUEST_CODE, requestCode);
        payload.putInt(PARAM_TRANSITION_START_ANIMATION, startAnimId);
        payload.putInt(PARAM_TRANSITION_END_ANIMATION, endAnimId);

        TopicService.dispatchEvent(context, TOPIC_ID_START_ACTIVITY_FOR_RESULT, payload, Sticky.NONE);
    }

    public boolean subStartActivityForResult() {
        return register(TOPIC_ID_START_ACTIVITY_FOR_RESULT);
    }

    public boolean clearStartActivityForResult() {
        return clearTopic(TOPIC_ID_START_ACTIVITY_FOR_RESULT);
    }

    public static void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        Bundle payload = new Bundle();
        payload.putInt(PARAM_REQUEST_CODE, requestCode);
        payload.putInt(PARAM_RESULT_CODE, resultCode);
        payload.putParcelable(PARAM_INTENT, data);

        TopicService.dispatchEvent(context, TOPIC_ID_ON_ACTIVITY_RESULT + "/" + requestCode, payload, Sticky.TEMP);
    }

    /**
     * Subscribes to an activity result
     *
     * @param requestCode
     * @return
     */
    public boolean subOnActivityResult(int requestCode) {
        return register(TOPIC_ID_ON_ACTIVITY_RESULT + "/" + requestCode);
    }

    public void clearOnActivityResult(int requestCode) {
        clearTopic(TOPIC_ID_ON_ACTIVITY_RESULT + "/" + requestCode);
    }

    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public static abstract class RequestListener extends TopicClient.Listener {
        private static final String TAG = "ActivityResultClient.RequestListener";

        @Override
        public void onConnected() {
            getClient().subStartActivity();
            getClient().subStartActivityForResult();
        }

        @Override
        public void onEvent(String topicId, Parcelable payload) {
            if (topicId.startsWith(TOPIC_ID_START_ACTIVITY_FOR_RESULT)) {
                startActivityForResult((Bundle) payload);
            } else if (topicId.startsWith(TOPIC_ID_START_ACTIVITY)) {
                startActivity((Bundle) payload);
            }
        }

        public abstract Activity getActivity();

        public abstract ActivityResultClient getClient();

        /**
         * Override if you are an activity who will be making requests on behald of the rest of the app
         *
         * @param payload
         */
        private void startActivity(Bundle payload) {
            Log.v(TAG, "startActivity");
            Intent intent = payload.getParcelable(PARAM_INTENT);
            getActivity().startActivity(intent);

            int start = R.anim.activity_slide_in_right;
            int end = R.anim.activity_slide_out_left;

            if (payload.containsKey(PARAM_TRANSITION_START_ANIMATION))
                start = payload.getInt(PARAM_TRANSITION_START_ANIMATION);

            if (payload.containsKey(PARAM_TRANSITION_END_ANIMATION))
                end = payload.getInt(PARAM_TRANSITION_END_ANIMATION);

            if (start != 0 || end != 0) {
                getActivity().overridePendingTransition(start, end);
            }
        }

        private void startActivityForResult(Bundle bundle) {
            Intent intent = bundle.getParcelable(PARAM_INTENT);
            int requestCode = bundle.getInt(PARAM_REQUEST_CODE);

            getActivity().startActivityForResult(intent, requestCode);

            int start = R.anim.activity_slide_in_right;
            int end = R.anim.activity_slide_out_left;

            if (bundle.containsKey(PARAM_TRANSITION_START_ANIMATION))
                start = bundle.getInt(PARAM_TRANSITION_START_ANIMATION);

            if (bundle.containsKey(PARAM_TRANSITION_END_ANIMATION))
                end = bundle.getInt(PARAM_TRANSITION_END_ANIMATION);

            if (start != 0 || end != 0) {
                getActivity().overridePendingTransition(start, end);
            }
        }
    }

    public static abstract class ResultListener extends TopicClient.Listener {
        private static final String TAG = "ActivityResultClient.ResultListener";

        @Override
        public void onEvent(String topicId, Parcelable payload) {
            if (topicId.startsWith(TOPIC_ID_ON_ACTIVITY_RESULT)) {
                preOnActivityResult((Bundle) payload);
            }
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
