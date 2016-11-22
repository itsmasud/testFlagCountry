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
public class Timing implements Parcelable {
    private static final String TAG = "Timing";

    @Json
    final public String tag;
    @Json
    final public String category;
    @Json
    final public String label;
    @Json
    final public Integer timing;
    @Json
    final public String variable;
    @Json
    final public JsonArray extraContext;

    public Timing() {
        this.tag = null;
        this.category = null;
        this.label = null;
        this.timing = null;
        this.variable = null;
        this.extraContext = null;
    }

    public Timing(Builder builder) {
        this.tag = builder.tag;
        this.category = builder.category;
        this.label = builder.label;
        this.timing = builder.timing;
        this.variable = builder.variable;
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

    public static Timing fromJson(JsonObject object) {
        try {
            return Unserializer.unserializeObject(Timing.class, object);
        } catch (Exception e) {
            Log.v(TAG, e);
        }
        return null;
    }

    public static class Builder {
        private String tag;
        private String category;
        private String label;
        private Integer timing;
        private String variable;
        private JsonArray extraContext = new JsonArray();

        public Builder() {
        }

        public Timing build() {
            return new Timing(this);
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder label(String label) {
            this.label = label;
            return this;
        }

        public Builder timing(Integer timing) {
            this.timing = timing;
            return this;
        }

        public Builder variable(String variable) {
            this.variable = variable;
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
    public static Creator<Timing> CREATOR = new Creator<Timing>() {
        @Override
        public Timing createFromParcel(Parcel source) {
            return Timing.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
        }

        @Override
        public Timing[] newArray(int size) {
            return new Timing[size];
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
