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
import com.fieldnation.fntools.misc;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgen from swagger.
 */

public class Integration implements Parcelable {
    private static final String TAG = "Integration";

    @Json(name = "href")
    private String _href;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "name")
    private String _name;

    @Source
    private JsonObject SOURCE;

    public Integration() {
        SOURCE = new JsonObject();
    }

    public Integration(JsonObject obj) {
        SOURCE = obj;
    }

    public void setHref(String href) throws ParseException {
        _href = href;
        SOURCE.put("href", href);
    }

    public String getHref() {
        try {
            if (_href == null && SOURCE.has("href") && SOURCE.get("href") != null)
                _href = SOURCE.getString("href");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _href;
    }

    public Integration href(String href) throws ParseException {
        _href = href;
        SOURCE.put("href", href);
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        try {
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public Integration id(Integer id) throws ParseException {
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
            if (_name == null && SOURCE.has("name") && SOURCE.get("name") != null)
                _name = SOURCE.getString("name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _name;
    }

    public Integration name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Integration[] array) {
        JsonArray list = new JsonArray();
        for (Integration item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Integration[] fromJsonArray(JsonArray array) {
        Integration[] list = new Integration[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Integration fromJson(JsonObject obj) {
        try {
            return new Integration(obj);
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
    public static final Parcelable.Creator<Integration> CREATOR = new Parcelable.Creator<Integration>() {

        @Override
        public Integration createFromParcel(Parcel source) {
            try {
                return Integration.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Integration[] newArray(int size) {
            return new Integration[size];
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
