package com.fieldnation.data.bv2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class LocationValidation implements Parcelable {
    private static final String TAG = "LocationValidation";

    @Json(name = "is_valid")
    private Boolean _isValid;

    @Json(name = "messages")
    private String[] _messages;

    public LocationValidation() {
    }

    public void setIsValid(Boolean isValid) {
        _isValid = isValid;
    }

    public Boolean getIsValid() {
        return _isValid;
    }

    public LocationValidation isValid(Boolean isValid) {
        _isValid = isValid;
        return this;
    }

    public void setMessages(String[] messages) {
        _messages = messages;
    }

    public String[] getMessages() {
        return _messages;
    }

    public LocationValidation messages(String[] messages) {
        _messages = messages;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static LocationValidation fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(LocationValidation.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(LocationValidation locationValidation) {
        try {
            return Serializer.serializeObject(locationValidation);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<LocationValidation> CREATOR = new Parcelable.Creator<LocationValidation>() {

        @Override
        public LocationValidation createFromParcel(Parcel source) {
            try {
                return LocationValidation.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public LocationValidation[] newArray(int size) {
            return new LocationValidation[size];
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
