package com.fieldnation.analytics.trackers;

import android.content.Context;

import com.fieldnation.analytics.ElementAction;
import com.fieldnation.analytics.ElementType;
import com.fieldnation.analytics.SnowplowWrapper;
import com.fieldnation.analytics.contexts.SpSearchContext;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.fnanalytics.Event;
import com.fieldnation.fnanalytics.Screen;
import com.fieldnation.fnanalytics.Tracker;

/**
 * Created by mc on 1/4/17.
 */

public class SearchTracker {

    public enum Item {
        SEARCH_BAR("Search"),
        KEYBOARD("Search Keyboard");

        private String identity;

        Item(String identity) {
            this.identity = identity;
        }
    }

    private static final Screen SCREEN = new Screen.Builder().name("Search").tag(SnowplowWrapper.TAG).build();

    public static void onShow(Context context) {
        Tracker.screen(context, SCREEN);
    }

    public static void onSearch(Context context, Item item, long workOrderId) {
        Tracker.event(context, new Event.Builder()
                .addContext(new SpUIContext.Builder()
                        .page(SCREEN.name)
                        .elementIdentity(item.identity)
                        .elementAction(ElementAction.CLICK)
                        .elementType(item == Item.KEYBOARD ? ElementType.KEYBOARD_BUTTON : ElementType.BAR_BUTTON)
                        .build())
                .addContext(new SpSearchContext.Builder()
                        .name("ID")
                        .value(workOrderId + "")
                        .build())
                .action(ElementAction.CLICK)
                .build());
    }

    public static void onSearch(Context context, Item item, SavedSearchParams savedSearchParams) {
        Tracker.event(context, new Event.Builder()
                .addContext(new SpUIContext.Builder()
                        .page(SCREEN.name)
                        .elementIdentity(item.identity)
                        .elementAction(ElementAction.CLICK)
                        .elementType(ElementType.LIST_ITEM)
                        .build())
                .addContext(new SpSearchContext.Builder()
                        .name("Location")
                        .value(getLocationString(savedSearchParams))
                        .build())
                .addContext(new SpSearchContext.Builder()
                        .name("Status")
                        .value(savedSearchParams.title)
                        .build())
                .addContext(new SpSearchContext.Builder()
                        .name("Distance")
                        .value(savedSearchParams.radius + "")
                        .build())
                .action(ElementAction.CLICK)
                .build());
    }

    private static String getLocationString(SavedSearchParams savedSearchParams) {
        switch (savedSearchParams.uiLocationSpinner) {
            case 0:
                return "Profile";
            case 1:
                return "Current";
            case 2:
                return "Other";
            case 3:
                return "Remote";
            default:
                return "na";
        }
    }

}
