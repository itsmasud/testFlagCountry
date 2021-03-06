package com.fieldnation.analytics.trackers;

import android.content.Context;

import com.fieldnation.analytics.ElementAction;
import com.fieldnation.analytics.ElementType;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 1/4/17.
 */

public class SavedSearchTracker {
    private static final String SCREEN = "Saved Search";

    public static class Item implements TrackerBase.Identity {
        private static List<Item> valuesList = new LinkedList<>();
        private static Item[] valuesArray;

        public static final Item INBOX = new Item("Inbox");
        public static final Item SEARCH = new Item("Search");
        public static final Item ADDITIONAL_OPTIONS = new Item("Additional Options");

        private String identity;

        private Item(String identity) {
            this.identity = identity;
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
            return ElementType.BAR_BUTTON;
        }
    }

    public static void onShow(Context context) {
        TrackerBase.show(context, SCREEN, null);
    }

    public static void onListChanged(Context context, final String searchTitle) {
        TrackerBase.unstructuredEvent(context, new TrackerBase.Identity() {
            @Override
            public String page() {
                return SCREEN;
            }

            @Override
            public String identity() {
                return searchTitle + " Saved Search";
            }

            @Override
            public ElementAction elementAction() {
                return ElementAction.CLICK;
            }

            @Override
            public ElementType elementType() {
                return ElementType.LIST_ITEM;
            }
        });
    }

    public static void onClick(Context context, Item item) {
        TrackerBase.unstructuredEvent(context, item);
    }

    public static void test(Context context) {
        onShow(context);
        for (Item item : Item.values()) {
            onClick(context, item);
        }
    }
}
