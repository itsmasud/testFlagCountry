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
 * Created by mc on 1/5/17.
 */

public class CustomEvent implements Event {
    private static final String TAG = "CustomEvent";

    @Json
    final public String tag;
    @Json
    final public JsonArray extraContext;

    public CustomEvent() {
        this.tag = null;
        this.extraContext = null;
    }

    public CustomEvent(Builder builder) {
        this.tag = builder.tag;
        this.extraContext = builder.extraContext;
    }

    @Override
    public String getTag() {
        return tag;
    }

    public JsonObject toJson() {
        try {
            return Serializer.serializeObject(this);
        } catch (Exception e) {
            Log.v(TAG, e);
        }
        return null;
    }

    public static CustomEvent fromJson(JsonObject object) {
        try {
            return Unserializer.unserializeObject(CustomEvent.class, object);
        } catch (Exception e) {
            Log.v(TAG, e);
        }
        return null;
    }

    public static class Builder {
        private String tag;
        private JsonArray extraContext = new JsonArray();

        public Builder() {
        }

        public CustomEvent build() {
            return new CustomEvent(this);
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

    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static Creator<CustomEvent> CREATOR = new Creator<CustomEvent>() {
        @Override
        public CustomEvent createFromParcel(Parcel source) {
            return CustomEvent.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
        }

        @Override
        public CustomEvent[] newArray(int size) {
            return new CustomEvent[size];
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
