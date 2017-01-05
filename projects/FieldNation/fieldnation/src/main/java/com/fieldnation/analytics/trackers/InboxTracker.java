package com.fieldnation.analytics.trackers;

import android.content.Context;

import com.fieldnation.analytics.CustomEvent;
import com.fieldnation.analytics.ElementAction;
import com.fieldnation.analytics.ElementType;
import com.fieldnation.analytics.SnowplowWrapper;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.fnanalytics.Event;
import com.fieldnation.fnanalytics.Screen;
import com.fieldnation.fnanalytics.Tracker;

/**
 * Created by mc on 1/5/17.
 */

public class InboxTracker {
    private static final Screen SCREEN_MESSAGES = new Screen.Builder().name("Messages").tag(SnowplowWrapper.TAG).build();
    private static final Screen SCREEN_NOTIFICATIONS = new Screen.Builder().name("Notifications").tag(SnowplowWrapper.TAG).build();


    public enum Item {
        MESSAGES("Messages"),
        NOTIFICATIONS("Notifications");

        private String identity;

        Item(String identity) {
            this.identity = identity;
        }
    }

    public static void onShowMessages(Context context) {
        Tracker.screen(context, SCREEN_MESSAGES);
    }

    public static void onShowNotifications(Context context) {
        Tracker.screen(context, SCREEN_NOTIFICATIONS);
    }

    public static void onClickTab(Context context, Item item) {
        Tracker.event(context, new CustomEvent.Builder()
                .addContext(new SpUIContext.Builder()
                        .page(item == Item.MESSAGES ? SCREEN_MESSAGES.name : SCREEN_NOTIFICATIONS.name)
                        .elementIdentity(item.identity)
                        .elementAction(ElementAction.CLICK)
                        .elementType(ElementType.TAB)
                        .build())
                .build());

    }

}
