package com.fieldnation.fnanalytics;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by Michael on 9/13/2016.
 */
public class Event implements Parcelable {
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

    public Event() {
        this.tag = null;
        this.category = null;
        this.action = null;
        this.label = null;
        this.property = null;
        this.value = null;
        this.extraContext = null;
    }

    public Event(Builder builder) {
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

    public static Event fromJson(JsonObject object) {
        try {
            return Unserializer.unserializeObject(Event.class, object);
        } catch (Exception e) {
            Log.v(TAG, e);
        }
        return null;
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

        public Event build() {
            return new Event(this);
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
    public static Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return Event.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
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
