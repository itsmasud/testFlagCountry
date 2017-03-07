package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class LocationValidation implements Parcelable {
    private static final String TAG = "LocationValidation";

    @Json(name = "is_valid")
    private Boolean _isValid;

    @Json(name = "messages")
    private String[] _messages;

    @Source
    private JsonObject SOURCE;

    public LocationValidation() {
        SOURCE = new JsonObject();
    }

    public LocationValidation(JsonObject obj) {
        SOURCE = obj;
    }

    public void setIsValid(Boolean isValid) throws ParseException {
        _isValid = isValid;
        SOURCE.put("is_valid", isValid);
    }

    public Boolean getIsValid() {
        try {
            if (_isValid != null)
                return _isValid;

            if (SOURCE.has("is_valid") && SOURCE.get("is_valid") != null)
                _isValid = SOURCE.getBoolean("is_valid");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _isValid;
    }

    public LocationValidation isValid(Boolean isValid) throws ParseException {
        _isValid = isValid;
        SOURCE.put("is_valid", isValid);
        return this;
    }

    public void setMessages(String[] messages) throws ParseException {
        _messages = messages;
        JsonArray ja = new JsonArray();
        for (String item : messages) {
            ja.add(item);
        }
        SOURCE.put("messages", ja);
    }

    public String[] getMessages() {
        try {
            if (_messages != null)
                return _messages;

            if (SOURCE.has("messages") && SOURCE.get("messages") != null) {
                JsonArray ja = SOURCE.getJsonArray("messages");
                _messages = ja.toArray(new String[ja.size()]);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _messages;
    }

    public LocationValidation messages(String[] messages) throws ParseException {
        _messages = messages;
        JsonArray ja = new JsonArray();
        for (String item : messages) {
            ja.add(item);
        }
        SOURCE.put("messages", ja, true);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(LocationValidation[] array) {
        JsonArray list = new JsonArray();
        for (LocationValidation item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static LocationValidation[] fromJsonArray(JsonArray array) {
        LocationValidation[] list = new LocationValidation[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static LocationValidation fromJson(JsonObject obj) {
        try {
            return new LocationValidation(obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}
