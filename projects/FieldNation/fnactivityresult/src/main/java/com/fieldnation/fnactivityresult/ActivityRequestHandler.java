package com.fieldnation.fnactivityresult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Pigeon;
import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.fnpigeon.Sticky;

/**
 * Created by mc on 8/9/17.
 */

public abstract class ActivityRequestHandler extends Pigeon implements ActivityResultConstants {
    private static final String TAG = "ActivityRequestHandler";

    public void sub() {
        PigeonRoost.sub(this, ADDRESS_START_ACTIVITY);
        PigeonRoost.sub(this, ADDRESS_START_ACTIVITY_FOR_RESULT);
    }

    public void unsub() {
        PigeonRoost.unsub(this, ADDRESS_START_ACTIVITY);
        PigeonRoost.unsub(this, ADDRESS_START_ACTIVITY_FOR_RESULT);
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle message = new Bundle();
        message.putInt(PARAM_REQUEST_CODE, requestCode);
        message.putInt(PARAM_RESULT_CODE, resultCode);
        message.putParcelable(PARAM_INTENT, data);

        PigeonRoost.sendMessage(ADDRESS_ON_ACTIVITY_RESULT, message, Sticky.TEMP);
    }

    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    @Override
    public void onMessage(String address, Parcelable message) {
        if (address.startsWith(ADDRESS_START_ACTIVITY_FOR_RESULT)) {
            startActivityForResult((Bundle) message);
        } else if (address.startsWith(ADDRESS_START_ACTIVITY)) {
            startActivity((Bundle) message);
        }
    }

    public abstract Activity getActivity();

    /**
     * Override if you are an activity who will be making requests on behald of the rest of the app
     *
     * @param payload
     */
    private void startActivity(Bundle payload) {
        PigeonRoost.clearAddressCache(ADDRESS_START_ACTIVITY);
        Log.v(TAG, "startActivity " + getActivity().getClass().getSimpleName());
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
        PigeonRoost.clearAddressCache(ADDRESS_START_ACTIVITY_FOR_RESULT);

        Intent intent = bundle.getParcelable(PARAM_INTENT);
        int requestCode = bundle.getInt(PARAM_REQUEST_CODE);
        Log.v(TAG, "startActivityForResult " + getActivity().getClass().getSimpleName() + " " + requestCode);

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

