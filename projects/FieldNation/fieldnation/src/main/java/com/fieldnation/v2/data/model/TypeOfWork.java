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

public class TypeOfWork implements Parcelable {
    private static final String TAG = "TypeOfWork";

    @Json(name = "id")
    private Integer _id;

    @Json(name = "industry")
    private String _industry;

    @Json(name = "name")
    private String _name;

    @Source
    private JsonObject SOURCE;

    public TypeOfWork() {
        SOURCE = new JsonObject();
    }

    public TypeOfWork(JsonObject obj) {
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

    public TypeOfWork id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setIndustry(String industry) throws ParseException {
        _industry = industry;
        SOURCE.put("industry", industry);
    }

    public String getIndustry() {
        try {
            if (_industry == null && SOURCE.has("industry") && SOURCE.get("industry") != null)
                _industry = SOURCE.getString("industry");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _industry;
    }

    public TypeOfWork industry(String industry) throws ParseException {
        _industry = industry;
        SOURCE.put("industry", industry);
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

    public TypeOfWork name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(TypeOfWork[] array) {
        JsonArray list = new JsonArray();
        for (TypeOfWork item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static TypeOfWork[] fromJsonArray(JsonArray array) {
        TypeOfWork[] list = new TypeOfWork[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static TypeOfWork fromJson(JsonObject obj) {
        try {
            return new TypeOfWork(obj);
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
    public static final Parcelable.Creator<TypeOfWork> CREATOR = new Parcelable.Creator<TypeOfWork>() {

        @Override
        public TypeOfWork createFromParcel(Parcel source) {
            try {
                return TypeOfWork.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public TypeOfWork[] newArray(int size) {
            return new TypeOfWork[size];
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
