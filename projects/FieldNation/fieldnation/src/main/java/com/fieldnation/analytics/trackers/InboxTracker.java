package com.fieldnation.analytics.trackers;

import android.content.Context;

import com.fieldnation.analytics.CustomEvent;
import com.fieldnation.analytics.ElementAction;
import com.fieldnation.analytics.ElementType;
import com.fieldnation.analytics.SnowplowWrapper;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.fnanalytics.Screen;
import com.fieldnation.fnanalytics.Tracker;

/**
 * Created by mc on 1/5/17.
 */

public class InboxTracker {
    private static final String SCREEN_MESSAGES = "Messages";
    private static final String SCREEN_NOTIFICATIONS = "Notifications";


    public enum Item {
        MESSAGES("Messages"),
        NOTIFICATIONS("Notifications");

        private String identity;

        Item(String identity) {
            this.identity = identity;
        }
    }

    public static void onShowMessages(Context context) {
        Tracker.screen(context,
                new Screen.Builder()
                        .name(SCREEN_MESSAGES)
                        .tag(SnowplowWrapper.TAG)
                        .addContext(new SpUIContext.Builder()
                                .page(SCREEN_MESSAGES)
                                .build()
                        )
                        .build());
    }

    public static void onShowNotifications(Context context) {
        Tracker.screen(context,
                new Screen.Builder()
                        .name(SCREEN_NOTIFICATIONS)
                        .tag(SnowplowWrapper.TAG)
                        .addContext(new SpUIContext.Builder()
                                .page(SCREEN_NOTIFICATIONS)
                                .build()
                        )
                        .build());
    }

    public static void onClickTab(Context context, Item item) {
        Tracker.event(context, new CustomEvent.Builder()
                .addContext(new SpUIContext.Builder()
                        .page(item == Item.MESSAGES ? SCREEN_MESSAGES : SCREEN_NOTIFICATIONS)
                        .elementIdentity(item.identity)
                        .elementAction(ElementAction.CLICK)
                        .elementType(ElementType.TAB)
                        .build())
                .build());

    }

    public static void test(Context context) {
        onShowMessages(context);
        onShowNotifications(context);
        for (Item item : Item.values()) {
            onClickTab(context, item);
        }
    }
}
