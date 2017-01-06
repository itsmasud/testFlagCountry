package com.fieldnation.analytics.trackers;

import android.content.Context;

import com.fieldnation.analytics.CustomEvent;
import com.fieldnation.analytics.ElementAction;
import com.fieldnation.analytics.ElementType;
import com.fieldnation.analytics.SnowplowWrapper;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.fnanalytics.Screen;
import com.fieldnation.fnanalytics.Tracker;

/**
 * Created by mc on 1/4/17.
 */

public class SavedSearchTracker {
    private static final Screen SCREEN = new Screen.Builder().name("Saved Search").tag(SnowplowWrapper.TAG).build();

    public enum Item {
        INBOX("Inbox"),
        SEARCH("Search"),
        ADDITIONAL_OPTIONS("Additional Options");

        private String identity;

        Item(String identity) {
            this.identity = identity;
        }
    }

    public static void onShow(Context context) {
        Tracker.screen(context, SCREEN);
    }

    public static void onListChanged(Context context, SavedSearchParams savedSearchParams) {
        Tracker.event(context, new CustomEvent.Builder()
                .addContext(new SpUIContext.Builder()
                        .page(SCREEN.name)
                        .elementIdentity(savedSearchParams.title + " Saved Search")
                        .elementAction(ElementAction.CLICK)
                        .elementType(ElementType.LIST_ITEM)
                        .build())
                .build());
    }

    public static void onClick(Context context, Item item) {
        Tracker.event(context, new CustomEvent.Builder()
                .addContext(new SpUIContext.Builder()
                        .page(SCREEN.name)
                        .elementIdentity(item.identity)
                        .elementAction(ElementAction.CLICK)
                        .elementType(ElementType.BAR_BUTTON)
                        .build())
                .build());
    }
}
