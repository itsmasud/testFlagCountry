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
import android.util.Log;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.server.Result;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegistrationIntentService extends IntentService {
    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            synchronized (TAG) {
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(getString(R.string.gcm_senderid), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                Log.i(TAG, "GCM Registration Token: " + token);

                JsonObject body = new JsonObject();
                body.put("ref", 1);
                body.put("record", "register-mobile-device");
                body.put("data.item_type", "gcm");
                body.put("data.device_id", token);
                body.put("data.user_id", GlobalState.getContext().getProfile().getUserId());

                new AsyncTaskEx<String, Object, Object>() {
                    @Override
                    protected Object doInBackground(String... params) {
                        try {
                            String body = params[0];

                            HttpURLConnection conn = null;
                            conn = (HttpURLConnection) new URL("https://" + getString(R.string.web_fn_hostname) + "/api/rest/v1/api/record").openConnection();

                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("ContentType", "application/json");

                            conn.setDoInput(false);
                            conn.setReadTimeout(10000);

                            conn.setDoOutput(true);
                            OutputStream out = conn.getOutputStream();
                            out.write(body.getBytes());
                            out.flush();
                            out.close();

                            try {
                                conn.connect();
                                new Result(conn);
                            } finally {
                                conn.disconnect();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        return null;
                    }
                }.executeEx("[" + body.toString() + "]");
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
        }
    }
}
