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

public class DrugTest implements Parcelable {
    private static final String TAG = "DrugTest";

    @Json(name = "expires")
    private String _expires;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "name")
    private String _name;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public DrugTest() {
    }

    public void setExpires(String expires) throws ParseException {
        _expires = expires;
        SOURCE.put("expires", expires);
    }

    public String getExpires() {
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
            return Unserializer.unserializeObject(DrugTest.class, obj);
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
}
