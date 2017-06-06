package com.fieldnation.analytics.trackers;

import android.content.Context;

import com.fieldnation.analytics.ElementAction;
import com.fieldnation.analytics.ElementType;
import com.fieldnation.analytics.contexts.SpSearchContext;
import com.fieldnation.fnanalytics.EventContext;
import com.fieldnation.v2.ui.search.FilterParams;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 1/4/17.
 */

public class SearchTracker {
    private static final String SCREEN = "Search";

    public static class Item implements TrackerBase.Identity, Cloneable {
        private static List<Item> valuesList = new LinkedList<>();
        private static Item[] valuesArray;

        public static final Item SEARCH_BAR = new Item("Search", ElementType.BAR_BUTTON);
        public static final Item KEYBOARD = new Item("Search Keyboard", ElementType.KEYBOARD_BUTTON);

        private String identity;
        private ElementType elementType;

        private Item(String identity, ElementType elementType) {
            this.identity = identity;
            this.elementType = elementType;
            valuesList.add(this);
        }

        public static Item[] values() {
            if (valuesArray != null && valuesArray.length == valuesList.size())
                return valuesArray;

            return valuesArray = valuesList.toArray(new Item[valuesList.size()]);
        }

        @Override
        public String page() {
            return SCREEN;
        }

        @Override
        public String identity() {
            return identity;
        }

        @Override
        public ElementAction elementAction() {
            return ElementAction.CLICK;
        }

        @Override
        public ElementType elementType() {
            return elementType;
        }

        public Item clone() {
            try {
                return (Item) super.clone();
            } catch (Exception e) {
                return null;
            }
        }
    }


    public static void onShow(Context context) {
        TrackerBase.show(context, SCREEN, null);
    }

    public static void onSearch(Context context, Item item, long workOrderId) {
        TrackerBase.unstructuredEvent(context, item,
                new EventContext[]{
                        new SpSearchContext.Builder()
                                .name("ID")
                                .value(workOrderId + "")
                                .build()
                });
    }

//    public static void onSearch(Context context, Item item, SavedSearchParams savedSearchParams) {
//        Item i = item.clone();
//        i.elementType = ElementType.LIST_ITEM;
//        TrackerBase.unstructuredEvent(context, item,
//                new EventContext[]{
//                        new SpSearchContext.Builder()
//                                .name("Location")
//                                .value(getLocationString(savedSearchParams))
//                                .build(),
//                        new SpSearchContext.Builder()
//                                .name("Status")
//                                .value(savedSearchParams.title)
//                                .build(),
//                        new SpSearchContext.Builder()
//                                .name("Distance")
//                                .value(savedSearchParams.radius + "")
//                                .build()
//                });
//    }

    public static void onSearch(Context context, Item item, FilterParams filterParams) {
        Item i = item.clone();
        i.elementType = ElementType.LIST_ITEM;
        TrackerBase.unstructuredEvent(context, item,
                new EventContext[]{
                        new SpSearchContext.Builder()
                                .name("Location")
                                .value(getLocationString(filterParams))
                                .build(),
                        new SpSearchContext.Builder()
                                .name("Status")
                                .value(filterParams.title)
                                .build(),
                        new SpSearchContext.Builder()
                                .name("Distance")
                                .value(filterParams.radius + "")
                                .build()
                });
    }

//    private static String getLocationString(SavedSearchParams savedSearchParams) {
//        switch (savedSearchParams.uiLocationSpinner) {
//            case 0:
//                return "Profile";
//            case 1:
//                return "Current";
//            case 2:
//                return "Other";
//            case 3:
//                return "Remote";
//            default:
//                return "na";
//        }
//    }

    private static String getLocationString(FilterParams filterParams) {
        switch (filterParams.uiLocationSpinner) {
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
//        SavedSearchParams[] list = SavedSearchClient.defaults;
//        for (Item item : Item.values()) {
//            onSearch(context, item, 1);
//            for (SavedSearchParams p : list) {
//                onSearch(context, item, p);
//            }
//        }
    }
}
