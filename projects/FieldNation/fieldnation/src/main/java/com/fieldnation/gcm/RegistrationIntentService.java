/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fieldnation.gcm;

import android.app.IntentService;
import android.content.Intent;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.UniqueTag;
import com.fieldnation.service.data.profile.ProfileClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;


public class RegistrationIntentService extends IntentService {
    private static final String STAG = "RegIntentService";
    private final String TAG = UniqueTag.makeTag(STAG);


    public RegistrationIntentService() {
        super(STAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            synchronized (TAG) {
                Log.v(TAG, "Sending GCM register");
                InstanceID instanceID = InstanceID.getInstance(RegistrationIntentService.this);
                String token = instanceID.getToken(getString(R.string.gcm_senderid),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                Log.i(TAG, "GCM Registration Token: " + token);

                ProfileClient.actionRegisterDevice(this, token);

                stopSelf();
            }
        } catch (Exception e) {
            Log.d(TAG, e);
        }
    }
}
