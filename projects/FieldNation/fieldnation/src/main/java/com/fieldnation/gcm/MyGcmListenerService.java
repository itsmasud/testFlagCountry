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

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.fieldnation.R;
import com.fieldnation.analytics.AnswersWrapper;
import com.fieldnation.analytics.EventAction;
import com.fieldnation.analytics.EventCategory;
import com.fieldnation.fnanalytics.Event;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.service.data.workorder.WorkorderTransactionBuilder;
import com.fieldnation.ui.workorder.WorkorderActivity;
import com.google.android.gms.gcm.GcmListenerService;

import java.util.Random;

public class MyGcmListenerService extends GcmListenerService {
    private static final String STAG = "MyGcmListenerService";
    private final String TAG = UniqueTag.makeTag(STAG);

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        Tracker.event(this,
                new Event.Builder()
                        .tag(AnswersWrapper.TAG)
                        .category(EventCategory.GCM)
                        .action(EventAction.PUSH_NOTIFICATION)
                        .build());

//        GlobalTopicClient.gcm(this, message);
        sendNotification(message);
    }
    // [END receive_message]

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        super.onDestroy();
    }


    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        try {
            JsonObject obj = new JsonObject(message);
            Log.v(TAG, obj.display());

            if (obj.getString("type").equals("READY_TO_GO")) {
                JsonObject positiveButton = null;
                JsonObject negativeButton = null;

                JsonArray buttons = obj.getJsonArray("buttons");
                for (int i = 0; i < buttons.size(); i++) {
                    JsonObject button = buttons.getJsonObject(i);
                    if (button.getString("type").equals("POSITIVE")) {
                        positiveButton = button;
                    } else if (button.getString("type").equals("NEGATIVE")) {
                        negativeButton = button;
                    }
                }
                // TODO, need to finish this
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                builder.setSmallIcon(R.drawable.ic_notif_logo);
                builder.setContentTitle(obj.getString("title"));
                builder.setContentText(obj.getString("body"));

                Intent workorderIntent = new Intent(this, WorkorderActivity.class);
                workorderIntent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, obj.getLong("workorder_id"));
                workorderIntent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_DETAILS);
                PendingIntent workorderPi = PendingIntent.getActivity(this, 0, workorderIntent, 0);

                builder.setContentIntent(workorderPi);

                if (negativeButton != null) {
                    builder.addAction(R.drawable.ic_notif_glass, negativeButton.getString("label"), workorderPi);
                }

                if (positiveButton != null) {
                    if (positiveButton.has("url") && positiveButton.getString("url").endsWith("confirm")) {
                        Log.v(TAG, "positiveButton1");
                        PendingIntent readyToGoPi = PendingIntent.getActivity(this, 0,
                                WorkorderActivity.makeIntentConfirm(this, obj.getLong("workorder_id")), 0);

                        builder.addAction(R.drawable.ic_notif_check, positiveButton.getString("label"), readyToGoPi);
                    } else {
                        Log.v(TAG, "positiveButton2");
                        PendingIntent readyToGoPi = PendingIntent.getService(this, 0,
                                WorkorderTransactionBuilder.actionReadyIntent(this, obj.getLong("workorder_id")), 0);

                        builder.addAction(R.drawable.ic_notif_check, positiveButton.getString("label"), readyToGoPi);
                    }
                }

                NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
                bigTextStyle.setBigContentTitle(obj.getString("title"));
                bigTextStyle.bigText(obj.getString("body") + "\nWork order " + obj.getString("workorder_id"));
                builder.setStyle(bigTextStyle);
                builder.setPriority(NotificationCompat.PRIORITY_MAX);

//                long Q = 400;
//                long E = 200;
//                long[] two_bits = new long[]{10, Q, 10, E, 10, E, 10, Q, 10, Q, Q, Q, 10, Q};
//                long[] ff_boss_fight = new long[]{0, 50, 100, 50, 100, 50, 100, 400, 100, 300, 100, 350, 50, 200, 100, 100, 50, 600};
//                long[] star_wars = new long[]{0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500};
                long[] hard = new long[]{0, 500};

                builder.setVibrate(hard);

                NotificationManagerCompat.from(this).notify(new Random().nextInt(), builder.build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
