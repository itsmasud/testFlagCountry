package com.fieldnation.analytics.trackers;

import android.content.Context;

import com.fieldnation.analytics.CustomEvent;
import com.fieldnation.analytics.ElementAction;
import com.fieldnation.analytics.ElementType;
import com.fieldnation.analytics.SnowplowWrapper;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.fnanalytics.Screen;
import com.fieldnation.fnanalytics.Tracker;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 1/4/17.
 */

public class AdditionalOptionsTracker {
    private static final String SCREEN = "Additional Options";

    public static class Item implements TrackerBase.Identity {
        private static List<Item> valuesList = new LinkedList<>();
        private static Item[] valuesArray;

        public static final Item PROFILE = new Item("Profile");
        public static final Item PAYMENTS = new Item("Payments");
        public static final Item SETTINGS = new Item("Settings");
        public static final Item LOG_OUT = new Item("Log Out");
        public static final Item CONTACT_US = new Item("Contact Us");
        public static final Item LEGAL = new Item("Legal");
        public static final Item APP_VERSION = new Item("App Version");

        private String identity;

        private Item(String identity) {
            this.identity = identity;
            valuesList.add(this);
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
            return ElementType.LIST_ITEM;
        }

        public static Item[] values() {
            if (valuesArray != null && valuesArray.length == valuesList.size())
                return valuesArray;

            return valuesArray = valuesList.toArray(new Item[valuesList.size()]);
        }
    }

    public static void onShow(Context context) {
        TrackerBase.show(context, SCREEN, null);
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
