package com.fieldnation.fnpermissions;

import android.os.Bundle;
import android.os.Parcelable;

import com.fieldnation.fnpigeon.Pigeon;
import com.fieldnation.fnpigeon.PigeonRoost;

/**
 * Created by mc on 8/10/17.
 */

public abstract class PermissionsResponseListener extends Pigeon {
    private static final String TAG = "PermissionsClient";

    private static final String TOPIC_ID_REQUESTS = TAG + ":TOPIC_ID_REQUESTS";
    private static final String TOPIC_ID_REQUEST_RESULT = TAG + ":TOPIC_ID_REQUEST_RESULT";
    private static final String TOPIC_ID_COMPLETE = TAG + ":TOPIC_ID_COMPLETE";
    private static final String TOPIC_ID_PROCESS_QUEUE = TAG + ":TOPIC_ID_PROCESS_QUEUE";

    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public void sub() {
        PigeonRoost.sub(this, TOPIC_ID_COMPLETE);
    }

    public void unsub() {
        PigeonRoost.unsub(this, TOPIC_ID_COMPLETE);
    }

    @Override
    public void onMessage(String address, Parcelable message) {
        if (address.equals(TOPIC_ID_COMPLETE)) {
            Bundle bundle = (Bundle) message;
            onComplete(bundle.getString("permission"),
                    bundle.getInt("grantResult"));
        }
    }

    public abstract void onComplete(String permission, int grantResult);
}
