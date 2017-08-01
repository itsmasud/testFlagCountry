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

public class WorkOrderRatingsBuyerWorkOrderCategories implements Parcelable {
    private static final String TAG = "WorkOrderRatingsBuyerWorkOrderCategories";

    @Json(name = "rating")
    private Integer _rating;

    @Json(name = "type")
    private TypeEnum _type;

    @Source
    private JsonObject SOURCE;

    public WorkOrderRatingsBuyerWorkOrderCategories() {
        SOURCE = new JsonObject();
    }

    public WorkOrderRatingsBuyerWorkOrderCategories(JsonObject obj) {
        SOURCE = obj;
    }

    public void setRating(Integer rating) throws ParseException {
        _rating = rating;
        SOURCE.put("rating", rating);
    }

    public Integer getRating() {
        try {
            if (_rating == null && SOURCE.has("rating") && SOURCE.get("rating") != null)
                _rating = SOURCE.getInt("rating");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _rating;
    }

    public WorkOrderRatingsBuyerWorkOrderCategories rating(Integer rating) throws ParseException {
        _rating = rating;
        SOURCE.put("rating", rating);
        return this;
    }

    public void setType(TypeEnum type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.toString());
    }

    public TypeEnum getType() {
        try {
            if (_type == null && SOURCE.has("type") && SOURCE.get("type") != null)
                _type = TypeEnum.fromString(SOURCE.getString("type"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _type;
    }

    public WorkOrderRatingsBuyerWorkOrderCategories type(TypeEnum type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.toString());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum TypeEnum {
        @Json(name = "professionalism")
        PROFESSIONALISM("professionalism"),
        @Json(name = "respect")
        RESPECT("respect"),
        @Json(name = "scope")
        SCOPE("scope");

        private String value;

        TypeEnum(String value) {
            this.value = value;
        }

        public static TypeEnum fromString(String value) {
            TypeEnum[] values = values();
            for (TypeEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static TypeEnum[] fromJsonArray(JsonArray jsonArray) {
            TypeEnum[] list = new TypeEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(WorkOrderRatingsBuyerWorkOrderCategories[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderRatingsBuyerWorkOrderCategories item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderRatingsBuyerWorkOrderCategories[] fromJsonArray(JsonArray array) {
        WorkOrderRatingsBuyerWorkOrderCategories[] list = new WorkOrderRatingsBuyerWorkOrderCategories[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderRatingsBuyerWorkOrderCategories fromJson(JsonObject obj) {
        try {
            return new WorkOrderRatingsBuyerWorkOrderCategories(obj);
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
    public static final Parcelable.Creator<WorkOrderRatingsBuyerWorkOrderCategories> CREATOR = new Parcelable.Creator<WorkOrderRatingsBuyerWorkOrderCategories>() {

        @Override
        public WorkOrderRatingsBuyerWorkOrderCategories createFromParcel(Parcel source) {
            try {
                return WorkOrderRatingsBuyerWorkOrderCategories.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderRatingsBuyerWorkOrderCategories[] newArray(int size) {
            return new WorkOrderRatingsBuyerWorkOrderCategories[size];
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
