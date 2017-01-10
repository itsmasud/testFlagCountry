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
 * Created by mc on 1/4/17.
 */

public class AdditionalOptionsTracker {
    private static final String SCREEN = "Additional Options";

    public enum Item {
        PROFILE("Profile"),
        PAYMENTS("Payments"),
        SETTINGS("Settings"),
        LOG_OUT("Log Out"),
        CONTACT_US("Contact Us"),
        LEGAL("Legal"),
        APP_VERSION("App Version");

        private String identity;

        Item(String identity) {
            this.identity = identity;
        }
    }

    public static void onShow(Context context) {
        Tracker.screen(context,
                new Screen.Builder()
                        .name(SCREEN)
                        .tag(SnowplowWrapper.TAG)
                        .addContext(new SpUIContext.Builder()
                                .page(SCREEN)
                                .build()
                        )
                        .build());
    }

    public static void onClick(Context context, Item item) {
        Tracker.event(context, new CustomEvent.Builder()
                .addContext(new SpUIContext.Builder()
                        .page(SCREEN)
                        .elementIdentity(item.identity)
                        .elementAction(ElementAction.CLICK)
                        .elementType(ElementType.LIST_ITEM)
                        .build())
                .build());
    }

    public static void test(Context context) {
        onShow(context);
        for (Item item : Item.values()) {
            onClick(context, item);
        }
    }

}
