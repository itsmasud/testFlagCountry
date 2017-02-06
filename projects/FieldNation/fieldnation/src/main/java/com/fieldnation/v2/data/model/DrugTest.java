package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger.
 */

public class DrugTest implements Parcelable {
    private static final String TAG = "DrugTest";

    @Json(name = "expires")
    private String _expires;

    @Json(name = "name")
    private String _name;

    @Json(name = "id")
    private Integer _id;

    public DrugTest() {
    }

    public void setExpires(String expires) {
        _expires = expires;
    }

    public String getExpires() {
        return _expires;
    }

    public DrugTest expires(String expires) {
        _expires = expires;
        return this;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public DrugTest name(String name) {
        _name = name;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public DrugTest id(Integer id) {
        _id = id;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
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

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(DrugTest drugTest) {
        try {
            return Serializer.serializeObject(drugTest);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
