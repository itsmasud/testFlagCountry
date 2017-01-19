package com.fieldnation.analytics;

import android.os.Parcel;

import com.fieldnation.fnanalytics.Event;
import com.fieldnation.fnanalytics.EventContext;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by Michael on 9/13/2016.
 */
public class SimpleEvent implements Event {
    private static final String TAG = "Event";

    @Json
    final public String tag;
    @Json
    final public String category;
    @Json
    final public String action;
    @Json
    final public String label;
    @Json
    final public String property;
    @Json
    final public Number value;
    @Json
    final public JsonArray extraContext;

    public SimpleEvent() {
        this.tag = null;
        this.category = null;
        this.action = null;
        this.label = null;
        this.property = null;
        this.value = null;
        this.extraContext = null;
    }

    public SimpleEvent(Builder builder) {
        this.tag = builder.tag;
        this.category = builder.category;
        this.action = builder.action;
        this.label = builder.label;
        this.property = builder.property;
        this.value = builder.value;
        this.extraContext = builder.extraContext;
    }

    public JsonObject toJson() {
        try {
            return Serializer.serializeObject(this);
        } catch (Exception e) {
            Log.v(TAG, e);
        }
        return null;
    }

    public static SimpleEvent fromJson(JsonObject object) {
        try {
            return Unserializer.unserializeObject(SimpleEvent.class, object);
        } catch (Exception e) {
            Log.v(TAG, e);
        }
        return null;
    }

    @Override
    public String getTag() {
        return tag;
    }

    public static class Builder {
        private String tag;
        private String category;
        private String action;
        private String label;
        private String property;
        private Number value;
        private JsonArray extraContext = new JsonArray();

        public Builder() {
        }

        public SimpleEvent build() {
            return new SimpleEvent(this);
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder addContext(EventContext object) {
            try {
                extraContext.add(object.toJson());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder action(String action) {
            this.action = action;
            return this;
        }

        public Builder label(String label) {
            this.label = label;
            return this;
        }

        public Builder property(String property) {
            this.property = property;
            return this;
        }

        public Builder value(Number value) {
            this.value = value;
            return this;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static Creator<SimpleEvent> CREATOR = new Creator<SimpleEvent>() {
        @Override
        public SimpleEvent createFromParcel(Parcel source) {
            return SimpleEvent.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
        }

        @Override
        public SimpleEvent[] newArray(int size) {
            return new SimpleEvent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(toJson(), flags);
    }
}
