package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class Translation implements Parcelable {
    private static final String TAG = "Translation";

    @Json(name = "id")
    private String _id;

    @Json(name = "value")
    private String _value;

    @Source
    private JsonObject SOURCE;

    public Translation() {
        SOURCE = new JsonObject();
    }

    public Translation(JsonObject obj) {
        SOURCE = obj;
    }

    public void setId(String id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public String getId() {
        try {
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getString("id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public Translation id(String id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setValue(String value) throws ParseException {
        _value = value;
        SOURCE.put("value", value);
    }

    public String getValue() {
        try {
            if (_value == null && SOURCE.has("value") && SOURCE.get("value") != null)
                _value = SOURCE.getString("value");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _value;
    }

    public Translation value(String value) throws ParseException {
        _value = value;
        SOURCE.put("value", value);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Translation[] array) {
        JsonArray list = new JsonArray();
        for (Translation item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Translation[] fromJsonArray(JsonArray array) {
        Translation[] list = new Translation[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Translation fromJson(JsonObject obj) {
        try {
            return new Translation(obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Creator<Translation> CREATOR = new Creator<Translation>() {

        @Override
        public Translation createFromParcel(Parcel source) {
            try {
                return Translation.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Translation[] newArray(int size) {
            return new Translation[size];
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

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

}
