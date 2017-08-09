package com.fieldnation.fnactivityresult;

import android.content.Intent;
import android.os.Bundle;

import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.fnpigeon.Sticky;

/**
 * Created by mc on 8/9/17.
 */

public class ActivityClient implements ActivityResultConstants {
    
    public static void startActivity(Intent intent) {
        Bundle payload = new Bundle();
        payload.putParcelable(PARAM_INTENT, intent);

        PigeonRoost.sendMessage(ADDRESS_START_ACTIVITY, payload, Sticky.NONE);
    }

    public static void startActivity(Intent intent, int startAnimId, int endAnimId) {
        Bundle payload = new Bundle();
        payload.putParcelable(PARAM_INTENT, intent);
        payload.putInt(PARAM_TRANSITION_START_ANIMATION, startAnimId);
        payload.putInt(PARAM_TRANSITION_END_ANIMATION, endAnimId);

        PigeonRoost.sendMessage(ADDRESS_START_ACTIVITY, payload, Sticky.NONE);
    }

    // For Result stuff
    public static void startActivityForResult(Intent intent, int requestCode) {
        Bundle payload = new Bundle();
        payload.putParcelable(PARAM_INTENT, intent);
        payload.putInt(PARAM_REQUEST_CODE, requestCode);

        PigeonRoost.sendMessage(ADDRESS_START_ACTIVITY_FOR_RESULT, payload, Sticky.NONE);
    }

    public static void startActivityForResult(Intent intent, int requestCode, int startAnimId, int endAnimId) {
        Bundle payload = new Bundle();
        payload.putParcelable(PARAM_INTENT, intent);
        payload.putInt(PARAM_REQUEST_CODE, requestCode);
        payload.putInt(PARAM_TRANSITION_START_ANIMATION, startAnimId);
        payload.putInt(PARAM_TRANSITION_END_ANIMATION, endAnimId);

        PigeonRoost.sendMessage(ADDRESS_START_ACTIVITY_FOR_RESULT, payload, Sticky.NONE);
    }
}
