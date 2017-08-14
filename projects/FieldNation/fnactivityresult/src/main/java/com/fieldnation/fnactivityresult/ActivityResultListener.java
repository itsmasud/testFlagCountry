package com.fieldnation.fnactivityresult;

import android.content.Intent;
import android.os.Bundle;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Pigeon;
import com.fieldnation.fnpigeon.PigeonRoost;

/**
 * Created by mc on 8/9/17.
 */

public class ActivityResultListener extends Pigeon implements ActivityResultConstants {
    private static final String TAG = "ActivityResultListener";

    public void sub() {
        PigeonRoost.sub(this, ADDRESS_ON_ACTIVITY_RESULT);
    }

    public void unsub() {
        PigeonRoost.unsub(this, ADDRESS_ON_ACTIVITY_RESULT);
    }

    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    @Override
    public void onMessage(String address, Object message) {
        if (address.startsWith(ADDRESS_ON_ACTIVITY_RESULT)) {
            preOnActivityResult((Bundle) message);
        }
    }

    private void preOnActivityResult(Bundle bundle) {
        Log.v(TAG, "preOnActivityResult");
        if (onActivityResult(
                bundle.getInt(PARAM_REQUEST_CODE),
                bundle.getInt(PARAM_RESULT_CODE),
                (Intent) bundle.getParcelable(PARAM_INTENT))) {
            PigeonRoost.clearAddressCache(ADDRESS_ON_ACTIVITY_RESULT);
        }
    }

    /**
     * Override if you want to receive results from a startActivityForResult call
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }
}
