package com.fieldnation.analytics.trackers;

import android.content.Context;

import com.fieldnation.analytics.CustomEvent;
import com.fieldnation.analytics.ElementAction;
import com.fieldnation.analytics.ElementType;
import com.fieldnation.analytics.SnowplowWrapper;
import com.fieldnation.analytics.contexts.SpSearchContext;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.fnanalytics.Screen;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.service.data.savedsearch.SavedSearchClient;

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

    private static final String SCREEN = "Search";

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

    public static void onSearch(Context context, Item item, long workOrderId) {
        Tracker.event(context, new CustomEvent.Builder()
                .addContext(new SpUIContext.Builder()
                        .page(SCREEN)
                        .elementIdentity(item.identity)
                        .elementAction(ElementAction.CLICK)
                        .elementType(item == Item.KEYBOARD ? ElementType.KEYBOARD_BUTTON : ElementType.BAR_BUTTON)
                        .build())
                .addContext(new SpSearchContext.Builder()
                        .name("ID")
                        .value(workOrderId + "")
                        .build())
                .build());
    }

    public static void onSearch(Context context, Item item, SavedSearchParams savedSearchParams) {
        Tracker.event(context, new CustomEvent.Builder()
                .addContext(new SpUIContext.Builder()
                        .page(SCREEN)
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

    public static void test(Context context) {
        onShow(context);
        SavedSearchParams[] list = SavedSearchClient.defaults;
        for (Item item : Item.values()) {
            onSearch(context, item, 1);
            for (SavedSearchParams p : list) {
                onSearch(context, item, p);
            }
        }

    }
}
