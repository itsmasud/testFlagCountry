package com.fieldnation.analytics;

import android.content.Context;

import com.fieldnation.BuildConfig;
import com.fieldnation.R;
import com.fieldnation.fnanalytics.EventContext;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.snowplowanalytics.snowplow.tracker.payload.SelfDescribingJson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael on 9/14/2016.
 */
public class SpUIContext implements EventContext {
    public static final String TAG = "SpUIContext";

    @Json
    public final String tag = TAG;
    @Json
    public final String page;
    @Json
    public final String elementType;
    @Json
    public final String elementAction;
    @Json
    public final String elementIdentity;

    public SpUIContext() {
        this.page = null;
        this.elementType = null;
        this.elementAction = null;
        this.elementIdentity = null;
    }

    public SpUIContext(Builder builder) {
        this.page = builder.page;
        this.elementType = builder.elementType;
        this.elementAction = builder.elementAction;
        this.elementIdentity = builder.elementIdentity;
    }

    @Override
    public String getContextName() {
        return TAG;
    }

    public SelfDescribingJson toSelfDescribingJson(Context context) {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("interface", "android");
        dataMap.put("version", (BuildConfig.VERSION_NAME + "-" + BuildConfig.BUILD_FLAVOR_NAME).trim());

        dataMap.put("page", "na");
        dataMap.put("element_type", "na"); // button, switch, link, card... etc..
        dataMap.put("element_action", "na"); // click, swipe,
        dataMap.put("element_identity", "na"); // differentiates different widgets on the same page

        if (page != null)
            dataMap.put("page", page);
        if (elementType != null)
            dataMap.put("element_type", elementType);
        if (elementAction != null)
            dataMap.put("element_action", elementAction);
        if (elementIdentity != null)
            dataMap.put("element_identity", elementIdentity);

        return new SelfDescribingJson(context.getString(R.string.sp_ui_context_schema_uri), dataMap);
    }

    public JsonObject toJson() {
        try {
            return Serializer.serializeObject(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SpUIContext fromJson(JsonObject object) {
        try {
            return Unserializer.unserializeObject(SpUIContext.class, object.getJsonObject(TAG));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class Builder {
        private String page;
        private String elementType;
        private String elementAction;
        private String elementIdentity;

        public Builder() {
        }

        public SpUIContext build() {
            return new SpUIContext(this);
        }

        public Builder page(String page) {
            this.page = page;
            return this;
        }

        public Builder elementType(String elementType) {
            this.elementType = elementType;
            return this;
        }

        public Builder elementAction(String elementAction) {
            this.elementAction = elementAction;
            return this;
        }

        public Builder elementIdentity(String elementIdentity) {
            this.elementIdentity = elementIdentity;
            return this;
        }
    }
}