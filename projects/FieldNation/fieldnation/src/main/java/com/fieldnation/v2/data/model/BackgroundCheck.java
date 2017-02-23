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

public class BackgroundCheck implements Parcelable {
    private static final String TAG = "BackgroundCheck";

    @Json(name = "expires")
    private String _expires;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "name")
    private String _name;

    @Source
    private JsonObject SOURCE;

    public BackgroundCheck() {
        SOURCE = new JsonObject();
    }

    public BackgroundCheck(JsonObject obj) {
        SOURCE = obj;
    }

    public void setExpires(String expires) throws ParseException {
        _expires = expires;
        SOURCE.put("expires", expires);
    }

    public String getExpires() {
        try {
            if (_expires != null)
                return _expires;

            if (SOURCE.has("expires") && SOURCE.get("expires") != null)
                _expires = SOURCE.getString("expires");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _expires;
    }

    public BackgroundCheck expires(String expires) throws ParseException {
        _expires = expires;
        SOURCE.put("expires", expires);
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        try {
            if (_id != null)
                return _id;

            if (SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public BackgroundCheck id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setName(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
    }

    public String getName() {
        try {
            if (_name != null)
                return _name;

            if (SOURCE.has("name") && SOURCE.get("name") != null)
                _name = SOURCE.getString("name");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _name;
    }

    public BackgroundCheck name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(BackgroundCheck[] array) {
        JsonArray list = new JsonArray();
        for (BackgroundCheck item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static BackgroundCheck[] fromJsonArray(JsonArray array) {
        BackgroundCheck[] list = new BackgroundCheck[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static BackgroundCheck fromJson(JsonObject obj) {
        try {
            return new BackgroundCheck(obj);
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
    public static final Parcelable.Creator<BackgroundCheck> CREATOR = new Parcelable.Creator<BackgroundCheck>() {

        @Override
        public BackgroundCheck createFromParcel(Parcel source) {
            try {
                return BackgroundCheck.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public BackgroundCheck[] newArray(int size) {
            return new BackgroundCheck[size];
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
