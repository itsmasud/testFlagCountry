package com.fieldnation.fnpermissions;

import android.os.Bundle;

import com.fieldnation.fnpigeon.Pigeon;
import com.fieldnation.fnpigeon.PigeonRoost;

/**
 * Created by mc on 8/10/17.
 */

public abstract class PermissionsResponseListener extends Pigeon implements Constants {
    private static final String TAG = "PermissionsClient";


    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public void sub() {
        PigeonRoost.sub(this, ADDRESS_COMPLETE);
    }

    public void unsub() {
        PigeonRoost.unsub(this, ADDRESS_COMPLETE);
    }

    @Override
    public void onMessage(String address, Object message) {
        if (address.equals(ADDRESS_COMPLETE)) {
            Bundle bundle = (Bundle) message;
            onComplete(bundle.getString("permission"),
                    bundle.getInt("grantResult"));
        }
    }

    public abstract void onComplete(String permission, int grantResult);
}
