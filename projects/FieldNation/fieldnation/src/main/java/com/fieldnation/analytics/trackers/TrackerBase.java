package com.fieldnation.analytics.trackers;

import android.content.Context;

import com.fieldnation.analytics.CustomEvent;
import com.fieldnation.analytics.ElementAction;
import com.fieldnation.analytics.ElementType;
import com.fieldnation.analytics.SimpleEvent;
import com.fieldnation.analytics.SnowplowWrapper;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.fnanalytics.EventContext;
import com.fieldnation.fnanalytics.Screen;
import com.fieldnation.fnanalytics.Tracker;

/**
 * Created by mc on 1/13/17.
 */

public class TrackerBase {

    public interface Identity {
        String page();

        String identity();

        ElementAction elementAction();

        ElementType elementType();
    }

    public interface Action {
        String action();

        String category();
    }

    public static void show(Context context, String name, EventContext extraContext) {
        Screen.Builder builder = new Screen.Builder()
                .tag(SnowplowWrapper.TAG)
                .name(name)
                .addContext(new SpUIContext.Builder()
                        .page(name)
                        .build());

        if (extraContext != null)
            builder.addContext(extraContext);

        Tracker.screen(context, builder.build());
    }

    public static void event(Context context, Identity identity, Action action, String property, String label, EventContext extraContext) {
        SimpleEvent.Builder builder = new SimpleEvent.Builder()
                .action(action.action())
                .category(action.category())
                .property(property)
                .label(label)
                .addContext(new SpUIContext.Builder()
                        .page(identity.page())
                        .elementIdentity(identity.identity())
                        .elementType(identity.elementType().elementType)
                        .elementAction(identity.elementAction().elementAction)
                        .build());

        if (extraContext != null)
            builder.addContext(extraContext);

        Tracker.event(context, builder.build());
    }

    public static void unstructuredEvent(Context context, Identity identity) {
        unstructuredEvent(context, identity, null);
    }

    public static void unstructuredEvent(Context context, Identity identity, EventContext[] extraContexts) {
        CustomEvent.Builder builder = new CustomEvent.Builder()
                .addContext(new SpUIContext.Builder()
                        .page(identity.page())
                        .elementAction(identity.elementAction().elementAction)
                        .elementIdentity(identity.identity())
                        .elementType(identity.elementType().elementType)
                        .build());

        if (extraContexts != null && extraContexts.length > 0) {
            for (EventContext ev : extraContexts) {
                builder.addContext(ev);
            }
        }

        Tracker.event(context, builder.build());
    }
}
