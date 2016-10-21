package com.fieldnation.data.v2;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

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
     * @return range or business
     */
    public Type getType() {
        if (type.equals("range"))
            return Type.RANGE;

        return Type.BUSINESS;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public void setType(Type type) {
        this.type = type.name().toLowerCase();
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

    public enum Type {
        BUSINESS, RANGE;
    }
}
