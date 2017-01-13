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
 * Created by mc on 1/5/17.
 */

public class InboxTracker {
    private static final String SCREEN_MESSAGES = "Messages";
    private static final String SCREEN_NOTIFICATIONS = "Notifications";


    public static class Item implements TrackerBase.Identity {
        public static final Item MESSAGES = new Item(SCREEN_MESSAGES);
        public static final Item NOTIFICATIONS = new Item(SCREEN_NOTIFICATIONS);

        private String identity;

        private static List<Item> valueList = new LinkedList<>();
        private static Item[] valuesArray;

        private Item(String identity) {
            this.identity = identity;
            valueList.add(this);
        }

        public static Item[] values() {
            if (valuesArray != null && valuesArray.length == valueList.size())
                return valuesArray;

            return valuesArray = valueList.toArray(new Item[valueList.size()]);
        }

        @Override
        public String page() {
            return identity;
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
            return ElementType.TAB;
        }
    }

    public static void onShowMessages(Context context) {
        TrackerBase.show(context, SCREEN_MESSAGES, null);
    }

    public static void onShowNotifications(Context context) {
        TrackerBase.show(context, SCREEN_NOTIFICATIONS, null);
    }

    public static void onClickTab(Context context, Item item) {
        TrackerBase.unstructuredEvent(context, item);
    }

    public static void test(Context context) {
        onShowMessages(context);
        onShowNotifications(context);
        for (Item item : Item.values()) {
            onClickTab(context, item);
        }
    }
}
