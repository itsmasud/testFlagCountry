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

public class DrugTest implements Parcelable {
    private static final String TAG = "DrugTest";

    @Json(name = "expires")
    private String _expires;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "name")
    private String _name;

    @Source
    private JsonObject SOURCE;

    public DrugTest() {
        SOURCE = new JsonObject();
    }

    public DrugTest(JsonObject obj) {
        SOURCE = obj;
    }

    public void setExpires(String expires) throws ParseException {
        _expires = expires;
        SOURCE.put("expires", expires);
    }

    public String getExpires() {
        try {
            if (_expires == null && SOURCE.has("expires") && SOURCE.get("expires") != null)
                _expires = SOURCE.getString("expires");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _expires;
    }

    public DrugTest expires(String expires) throws ParseException {
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
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public DrugTest id(Integer id) throws ParseException {
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

    public DrugTest name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(DrugTest[] array) {
        JsonArray list = new JsonArray();
        for (DrugTest item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static DrugTest[] fromJsonArray(JsonArray array) {
        DrugTest[] list = new DrugTest[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static DrugTest fromJson(JsonObject obj) {
        try {
            return new DrugTest(obj);
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
    public static final Parcelable.Creator<DrugTest> CREATOR = new Parcelable.Creator<DrugTest>() {

        @Override
        public DrugTest createFromParcel(Parcel source) {
            try {
                return DrugTest.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public DrugTest[] newArray(int size) {
            return new DrugTest[size];
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
