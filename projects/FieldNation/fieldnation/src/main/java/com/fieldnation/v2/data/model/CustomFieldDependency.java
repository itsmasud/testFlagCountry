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

public class CustomFieldDependency implements Parcelable {
    private static final String TAG = "CustomFieldDependency";

    @Json(name = "id")
    private Integer _id;

    @Json(name = "operator")
    private OperatorEnum _operator;

    @Json(name = "value")
    private String _value;

    public CustomFieldDependency() {
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public CustomFieldDependency id(Integer id) {
        _id = id;
        return this;
    }

    public void setOperator(OperatorEnum operator) {
        _operator = operator;
    }

    public OperatorEnum getOperator() {
        return _operator;
    }

    public CustomFieldDependency operator(OperatorEnum operator) {
        _operator = operator;
        return this;
    }

    public void setValue(String value) {
        _value = value;
    }

    public String getValue() {
        return _value;
    }

    public CustomFieldDependency value(String value) {
        _value = value;
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum OperatorEnum {
        @Json(name = "equals")
        EQUALS("equals"),
        @Json(name = "greater_than")
        GREATER_THAN("greater_than"),
        @Json(name = "greater_than_equals")
        GREATER_THAN_EQUALS("greater_than_equals"),
        @Json(name = "less_than")
        LESS_THAN("less_than"),
        @Json(name = "less_than_equals")
        LESS_THAN_EQUALS("less_than_equals");

        private String value;

        OperatorEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CustomFieldDependency[] fromJsonArray(JsonArray array) {
        CustomFieldDependency[] list = new CustomFieldDependency[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static CustomFieldDependency fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CustomFieldDependency.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CustomFieldDependency customFieldDependency) {
        try {
            return Serializer.serializeObject(customFieldDependency);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<CustomFieldDependency> CREATOR = new Parcelable.Creator<CustomFieldDependency>() {

        @Override
        public CustomFieldDependency createFromParcel(Parcel source) {
            try {
                return CustomFieldDependency.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public CustomFieldDependency[] newArray(int size) {
            return new CustomFieldDependency[size];
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
