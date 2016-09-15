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
public class Screen implements Parcelable {
    private static final String TAG = "Screen";

    @Json
    final public String tag;
    @Json
    final public String name;
    @Json
    final public JsonArray extraContext;

    public Screen() {
        this.tag = null;
        this.name = null;
        this.extraContext = null;
    }

    public Screen(Screen.Builder builder) {
        this.tag = builder.tag;
        this.name = builder.name;
        this.extraContext = builder.extraContext;
    }

    public static class Builder {
        private String tag;
        private String name;
        private JsonArray extraContext = new JsonArray();

        public Builder() {
        }

        public Screen build() {
            return new Screen(this);
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder addContext(EventContext object) {
            try {
                extraContext.add(object.toJson());
            } catch (Exception e) {
                Log.v(TAG, e);
            }
            return this;
        }
    }

    public JsonObject toJson() {
        try {
            return Serializer.serializeObject(this);
        } catch (Exception e) {
            Log.v(TAG, e);
        }
        return null;
    }

    public static Screen fromJson(JsonObject object) {
        try {
            return Unserializer.unserializeObject(Screen.class, object);
        } catch (Exception e) {
            Log.v(TAG, e);
        }
        return null;
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static Parcelable.Creator<Screen> CREATOR = new Creator<Screen>() {
        @Override
        public Screen createFromParcel(Parcel source) {
            return Screen.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
        }

        @Override
        public Screen[] newArray(int size) {
            return new Screen[size];
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
