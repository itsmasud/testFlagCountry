package com.fieldnation.data.v2;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.Unserializer;
import com.fieldnation.json.annotations.Json;

/**
 * Created by Michael on 8/19/2016.
 */
public class Range implements Parcelable {
    private static final String TAG = "Range";

    @Json
    private String end;
    @Json
    private String begin;
    @Json
    private String type; //range or business

    public Range() {
    }

    public String getEnd() {
        return end;
    }

    public String getBegin() {
        return begin;
    }

    /**
     *
     * @return range or business
     */
    public String getType() {
        return type;
    }

    /*-*************************************-*/
    /*-			JSON Implementation			-*/
    /*-*************************************-*/
    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Range range) {
        try {
            return Serializer.serializeObject(range);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Range fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Range.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Range> CREATOR = new Parcelable.Creator<Range>() {

        @Override
        public Range createFromParcel(Parcel source) {
            try {
                return Range.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Range[] newArray(int size) {
            return new Range[size];
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
