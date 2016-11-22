package com.fieldnation.data.v2;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by Michael on 7/21/2016.
 */
public class Org implements Parcelable {
    private static final String TAG = "Org";

    @Json
    private Long id;
    @Json
    private String name;

    public Org() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Org org) {
        try {
            return Serializer.serializeObject(org);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Org fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Org.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Org> CREATOR = new Parcelable.Creator<Org>() {

        @Override
        public Org createFromParcel(Parcel source) {
            try {
                return Org.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Org[] newArray(int size) {
            return new Org[size];
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
