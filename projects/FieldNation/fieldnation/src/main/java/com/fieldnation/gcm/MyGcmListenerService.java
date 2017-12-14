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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.fieldnation.App;
import com.fieldnation.NotificationDef;
import com.fieldnation.R;
import com.fieldnation.analytics.AnswersWrapper;
import com.fieldnation.analytics.EventAction;
import com.fieldnation.analytics.EventCategory;
import com.fieldnation.analytics.SimpleEvent;
import com.fieldnation.fnanalytics.Event;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.service.AnalyticsPassThroughService;
import com.fieldnation.service.data.workorder.WorkorderTransactionBuilder;
import com.fieldnation.v2.ui.nav.NavActivity;
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

    private static final Event VISITED_EVENT = new SimpleEvent.Builder()
            .category(EventCategory.GCM)
            .action(EventAction.PUSH_NOTIFICATION_INTERACTED)
            .build();

    private static final int CONFIRM_PUSH_NOTIFICATION = App.secureRandom.nextInt();

    public static void clearConfirmPush(Context context) {
        NotificationManagerCompat.from(context).cancel(CONFIRM_PUSH_NOTIFICATION);
    }

    /**
     * Called when message is received.
     * * @param from SenderID of the sender.
     *
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        Tracker.event(this,
                new SimpleEvent.Builder()
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

    private PendingIntent getIntentFromAction(Action action, int notificationId) {
        switch (action.getType()) {
            case VIEW: {
                switch (action.getObject()) {
                    case "wo": {
                        Intent workorderIntent = NavActivity.intentShowWorkOrder(this, Integer.parseInt(action.getId()));
                        PendingIntent pi = PendingIntent.getActivity(this, App.secureRandom.nextInt(), workorderIntent, 0);
                        return AnalyticsPassThroughService.createPendingIntent(this, VISITED_EVENT, pi, notificationId);
                    }
                    default:
                        break;
                }
                break;
            }

            case READY: {
                switch (action.getObject()) {
                    case "wo": {
                        return AnalyticsPassThroughService.createPendingIntent(this, VISITED_EVENT,
                                WorkorderTransactionBuilder.actionReadyIntent(Long.parseLong(action.getId())), notificationId);
                    }
                    default:
                        break;
                }
                break;
            }

            case CONFIRM: {
                switch (action.getObject()) {
                    case "wo": {
                        PendingIntent pi = PendingIntent.getActivity(this, App.secureRandom.nextInt(),
                                NavActivity.intentShowWorkOrder(this, Integer.parseInt(action.getId()), NavActivity.ACTION_CONFIRM), 0);
                        return AnalyticsPassThroughService.createPendingIntent(this, VISITED_EVENT, pi, notificationId);
                    }
                    default:
                        break;
                }
                break;
            }

            case CONFIRM_TOMORROW: {
                App.get().setNeedsConfirmation(true);
                PendingIntent pi = PendingIntent.getActivity(this, App.secureRandom.nextInt(),
                        NavActivity.startNewIntent(this), 0);
                return AnalyticsPassThroughService.createPendingIntent(this, VISITED_EVENT, pi, notificationId);
            }
        }
        return null;
    }

    private void buildPushNotification(GcmMessage gcmMessage) {
        int id = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder builder = new Notification.Builder(this, NotificationDef.PUSH_NOTIFICATION_CHANNEL);
            builder.setSmallIcon(R.drawable.ic_notif_logo);
            builder.setContentTitle(gcmMessage.title);
            builder.setContentText(gcmMessage.body);

            // workOrderId
            // confirm or ready?
            PendingIntent primaryIntent = null;
            Action primaryAction = null;

            if (gcmMessage.actions != null && gcmMessage.actions.length > 0) {
                primaryAction = gcmMessage.actions[0];
                primaryIntent = getIntentFromAction(primaryAction, id);
            }

            if ((primaryAction != null && primaryAction.getType() == Action.ActionType.CONFIRM_TOMORROW))
                id = CONFIRM_PUSH_NOTIFICATION;
            else
                id = App.secureRandom.nextInt();

            // no buttons
            if (primaryIntent == null) {

                // primary only
            } else if (primaryIntent != null) {
                builder.setContentIntent(primaryIntent);
            }

            Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
            bigTextStyle.setBigContentTitle(gcmMessage.title);
            bigTextStyle.bigText(gcmMessage.body);
            builder.setStyle(bigTextStyle);

            Notification notification = builder.build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(id, notification);
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.drawable.ic_notif_logo);
            builder.setContentTitle(gcmMessage.title);
            builder.setContentText(gcmMessage.body);

            // workOrderId
            // confirm or ready?
            PendingIntent primaryIntent = null;
            Action primaryAction = null;

            if (gcmMessage.actions != null && gcmMessage.actions.length > 0) {
                primaryAction = gcmMessage.actions[0];
                primaryIntent = getIntentFromAction(primaryAction, id);
            }

            if ((primaryAction != null && primaryAction.getType() == Action.ActionType.CONFIRM_TOMORROW))
                id = CONFIRM_PUSH_NOTIFICATION;
            else
                id = App.secureRandom.nextInt();

            // no buttons
            if (primaryIntent == null) {

                // primary only
            } else if (primaryIntent != null) {
                builder.setContentIntent(primaryIntent);
            }

            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            bigTextStyle.setBigContentTitle(gcmMessage.title);
            bigTextStyle.bigText(gcmMessage.body);
            builder.setStyle(bigTextStyle);
            builder.setPriority(NotificationCompat.PRIORITY_MAX);
            builder.setVibrate(default_ringtone);

            Notification notification = builder.build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            NotificationManagerCompat.from(this).notify(id, notification);
        }
    }
}