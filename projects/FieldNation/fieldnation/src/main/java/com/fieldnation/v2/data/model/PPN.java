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

public class PPN implements Parcelable {
    private static final String TAG = "PPN";

    @Json(name = "id")
    private Integer _id;

    @Json(name = "members")
    private Integer _members;

    @Json(name = "name")
    private String _name;

    @Source
    private JsonObject SOURCE;

    public PPN() {
        SOURCE = new JsonObject();
    }

    public PPN(JsonObject obj) {
        SOURCE = obj;
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

    public PPN id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setMembers(Integer members) throws ParseException {
        _members = members;
        SOURCE.put("members", members);
    }

    public Integer getMembers() {
        try {
            if (_members == null && SOURCE.has("members") && SOURCE.get("members") != null)
                _members = SOURCE.getInt("members");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _members;
    }

    public PPN members(Integer members) throws ParseException {
        _members = members;
        SOURCE.put("members", members);
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

    public PPN name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(PPN[] array) {
        JsonArray list = new JsonArray();
        for (PPN item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static PPN[] fromJsonArray(JsonArray array) {
        PPN[] list = new PPN[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static PPN fromJson(JsonObject obj) {
        try {
            return new PPN(obj);
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
    public static final Parcelable.Creator<PPN> CREATOR = new Parcelable.Creator<PPN>() {

        @Override
        public PPN createFromParcel(Parcel source) {
            try {
                return PPN.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PPN[] newArray(int size) {
            return new PPN[size];
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

    public boolean isSet() {
        return getId() != null && getId() != 0;
    }
}
