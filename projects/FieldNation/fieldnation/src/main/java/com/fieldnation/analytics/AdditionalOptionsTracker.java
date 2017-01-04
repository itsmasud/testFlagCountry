package com.fieldnation.analytics;

import android.content.Context;

import com.fieldnation.App;
import com.fieldnation.fnanalytics.Event;
import com.fieldnation.fnanalytics.Screen;
import com.fieldnation.fnanalytics.Tracker;

/**
 * Created by mc on 1/4/17.
 */

public class AdditionalOptionsTracker {
    private static final Screen SCREEN = new Screen.Builder().name("Additional Options").tag(SnowplowWrapper.TAG).build();


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
        Tracker.screen(context, SCREEN);
    }

    public static void onClick(Context context, Item item) {
        Tracker.event(context, new Event.Builder()
                .addContext(new SpUIContext.Builder()
                        .page(SCREEN.name)
                        .elementIdentity(item.identity)
                        .elementAction(ElementAction.CLICK)
                        .elementType(ElementType.LIST_ITEM)
                        .build())
                .action(ElementAction.CLICK)
                .build());
    }

}
