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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.AnswersWrapper;
import com.fieldnation.analytics.EventAction;
import com.fieldnation.analytics.EventCategory;
import com.fieldnation.data.v2.actions.Action;
import com.fieldnation.fnanalytics.Event;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.service.AnalyticsPassThroughService;
import com.fieldnation.service.data.workorder.WorkorderTransactionBuilder;
import com.fieldnation.ui.ncns.ConfirmActivity;
import com.fieldnation.ui.workorder.WorkorderActivity;
import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {
    private static final String STAG = "MyGcmListenerService";
    private final String TAG = UniqueTag.makeTag(STAG);

    //private static final long Q = 400;
    //private static final long E = 200;
    //private static final long[] two_bits = new long[]{10, Q, 10, E, 10, E, 10, Q, 10, Q, Q, Q, 10, Q};
    //private static final long[] ff_boss_fight = new long[]{0, 50, 100, 50, 100, 50, 100, 400, 100, 300, 100, 350, 50, 200, 100, 100, 50, 600};
    //private static final long[] star_wars = new long[]{0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500};
    private static final long[] default_ringtone = new long[]{0, 500};

    private static final Event VISITED_EVENT = new Event.Builder()
            .category(EventCategory.GCM)
            .action(EventAction.PUSH_NOTIFICATION_INTERACTED)
            .build();

    private static final int CONFIRM_PUSH_NOTIFICATION = App.secureRandom.nextInt();

    public static void clearConfirmPush(Context context) {
        NotificationManagerCompat.from(context).cancel(CONFIRM_PUSH_NOTIFICATION);
    }

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
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

        try {
            GcmMessage gcmMessage = GcmMessage.fromJson(new JsonObject(message));
            buildPushNotification(gcmMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private PendingIntent getIntentFromAction(Action action) {
        switch (action.getType()) {
            case VIEW: {
                switch (action.getObject()) {
                    case "wo": {
                        Intent workorderIntent = new Intent(this, WorkorderActivity.class);
                        workorderIntent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, Long.parseLong(action.getId()));
                        workorderIntent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_DETAILS);
                        PendingIntent pi = PendingIntent.getActivity(this, 0, workorderIntent, 0);
                        return AnalyticsPassThroughService.createPendingIntent(this, VISITED_EVENT, pi);
                    }
                    default:
                        break;
                }
                break;
            }

            case READY: {
                switch (action.getObject()) {
                    case "wo": {
                        PendingIntent pi = PendingIntent.getService(this, 0,
                                WorkorderTransactionBuilder.actionReadyIntent(this, Long.parseLong(action.getId())), 0);
                        return AnalyticsPassThroughService.createPendingIntent(this, VISITED_EVENT, pi);
                    }
                    default:
                        break;
                }
                break;
            }

            case CONFIRM: {
                switch (action.getObject()) {
                    case "wo": {
                        PendingIntent pi = PendingIntent.getActivity(this, 0,
                                WorkorderActivity.makeIntentConfirm(this, Long.parseLong(action.getId())), 0);
                        return AnalyticsPassThroughService.createPendingIntent(this, VISITED_EVENT, pi);
                    }
                    case "tomorrow": {
                        App.get().setNeedsConfirmation(true);
                        PendingIntent pi = PendingIntent.getActivity(this, 0,
                                ConfirmActivity.startNewIntent(this), 0);
                        return AnalyticsPassThroughService.createPendingIntent(this, VISITED_EVENT, pi);
                    }
                    default:
                        break;
                }
                break;
            }
        }
        return null;
    }

    private void buildPushNotification(GcmMessage gcmMessage) {
        // TODO, need to finish implementing this once we figure out how to send the other data

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_notif_logo);
        builder.setContentTitle(gcmMessage.title);
        builder.setContentText(gcmMessage.body);

        // workOrderId
        // confirm or ready?
        PendingIntent primaryIntent = null;
        Action primaryAction = null;
        PendingIntent secondaryIntent = null;
        Action secondaryAction = null;

        if (gcmMessage.actions != null) {
            if (gcmMessage.actions.getPrimary() != null && gcmMessage.actions.getPrimary().length > 0) {
                primaryAction = gcmMessage.actions.getPrimary()[0];
                primaryIntent = getIntentFromAction(primaryAction);
            }

            if (gcmMessage.actions.getSecondary() != null && gcmMessage.actions.getSecondary().length > 0) {
                secondaryAction = gcmMessage.actions.getSecondary()[0];
                secondaryIntent = getIntentFromAction(secondaryAction);
            }
        }

        // no buttons
        if (primaryIntent == null && secondaryIntent == null) {

            // primary only
        } else if (primaryIntent != null && secondaryIntent == null) {
            builder.setContentIntent(AnalyticsPassThroughService.createPendingIntent(
                    this, VISITED_EVENT, primaryIntent));

            // secondary only
        } else if (primaryIntent == null && secondaryIntent != null) {
            builder.setContentIntent(AnalyticsPassThroughService.createPendingIntent(
                    this, VISITED_EVENT, secondaryIntent));

        } else if (primaryIntent != null && secondaryIntent != null) {
            // have both
            // body
            builder.setContentIntent(AnalyticsPassThroughService.createPendingIntent(
                    this, VISITED_EVENT, secondaryIntent));

            // secondary button
            builder.addAction(R.drawable.ic_notif_glass, "View", secondaryIntent);

            // primary button
            String primaryButtonText = "";
            switch (primaryAction.getType()) {
                case CONFIRM:
                    primaryButtonText = "Confirm";
                    break;
                case READY:
                    primaryButtonText = "Ready";
                    break;
            }

            builder.addAction(R.drawable.ic_notif_check, primaryButtonText,
                    AnalyticsPassThroughService.createPendingIntent(this, VISITED_EVENT, primaryIntent));
        }

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(gcmMessage.title);
        bigTextStyle.bigText(gcmMessage.body);
        builder.setStyle(bigTextStyle);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setVibrate(default_ringtone);

        int id = 0;
        switch (gcmMessage.category) {
            case "CONFIRM_SCHEDULE":
                id = CONFIRM_PUSH_NOTIFICATION;
                break;
            default:
                id = App.secureRandom.nextInt();
                break;
        }

        NotificationManagerCompat.from(this).notify(id, builder.build());
    }
}