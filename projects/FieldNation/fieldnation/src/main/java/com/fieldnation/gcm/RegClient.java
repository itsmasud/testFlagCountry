package com.fieldnation.gcm;

import android.content.Context;

import com.fieldnation.R;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Pigeon;
import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fntools.AsyncTaskEx;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

/**
 * Created by mc on 10/25/17.
 */

public abstract class RegClient extends Pigeon {
    private static final String TAG = "RegClient";

    private static final String ADDRESS_HAVE_TOKEN = "RegClient:ADDRESS_HAVE_TOKEN";

    public void sub() {
        PigeonRoost.sub(this, ADDRESS_HAVE_TOKEN);
    }

    public void unsub() {
        PigeonRoost.unsub(this, ADDRESS_HAVE_TOKEN);
    }

    public static void requestToken(Context context) {
        new AsyncTaskEx<Object, Object, Object>() {

            @Override
            protected Object doInBackground(Object... params) {
                Context context = (Context) params[0];
                try {
                    synchronized (TAG) {
                        Log.v(TAG, "Sending GCM register");
                        InstanceID instanceID = InstanceID.getInstance(context);
                        String token = instanceID.getToken(context.getString(R.string.gcm_senderid),
                                GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                        Log.i(TAG, "GCM Registration Token: " + token);

                        PigeonRoost.sendMessage(ADDRESS_HAVE_TOKEN, token, Sticky.FOREVER);
                    }
                } catch (Exception e) {
                    Log.d(TAG, e);
                }
                return null;
            }
        }.executeEx(context);
    }

    @Override
    public void onMessage(String address, Object message) {
        onToken((String) message);
    }

    public abstract void onToken(String token);
}
