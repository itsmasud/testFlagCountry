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
 * Created by dmgen from swagger on 1/31/17.
 */

public class TypeOfWork implements Parcelable {
    private static final String TAG = "TypeOfWork";

    @Json(name = "name")
    private String _name;

    @Json(name = "industry")
    private String _industry;

    @Json(name = "id")
    private Integer _id;

    public TypeOfWork() {
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public TypeOfWork name(String name) {
        _name = name;
        return this;
    }

    public void setIndustry(String industry) {
        _industry = industry;
    }

    public String getIndustry() {
        return _industry;
    }

    public TypeOfWork industry(String industry) {
        _industry = industry;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public TypeOfWork id(Integer id) {
        _id = id;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static TypeOfWork[] fromJsonArray(JsonArray array) {
        TypeOfWork[] list = new TypeOfWork[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static TypeOfWork fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(TypeOfWork.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(TypeOfWork typeOfWork) {
        try {
            return Serializer.serializeObject(typeOfWork);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
