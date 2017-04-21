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

public class WorkOrderRatingsAssignedProviderWorkOrderCategories implements Parcelable {
    private static final String TAG = "WorkOrderRatingsAssignedProviderWorkOrderCategories";

    @Json(name = "type")
    private TypeEnum _type;

    @Json(name = "value")
    private Integer _value;

    @Source
    private JsonObject SOURCE;

    public WorkOrderRatingsAssignedProviderWorkOrderCategories() {
        SOURCE = new JsonObject();
    }

    public WorkOrderRatingsAssignedProviderWorkOrderCategories(JsonObject obj) {
        SOURCE = obj;
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

    public WorkOrderRatingsAssignedProviderWorkOrderCategories type(TypeEnum type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.toString());
        return this;
    }

    public void setValue(Integer value) throws ParseException {
        _value = value;
        SOURCE.put("value", value);
    }

    public Integer getValue() {
        try {
            if (_value == null && SOURCE.has("value") && SOURCE.get("value") != null)
                _value = SOURCE.getInt("value");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _value;
    }

    public WorkOrderRatingsAssignedProviderWorkOrderCategories value(Integer value) throws ParseException {
        _value = value;
        SOURCE.put("value", value);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum TypeEnum {
        @Json(name = "assignment_fulfilled")
        ASSIGNMENT_FULFILLED("assignment_fulfilled"),
        @Json(name = "follow_instructions")
        FOLLOW_INSTRUCTIONS("follow_instructions"),
        @Json(name = "on_time")
        ON_TIME("on_time"),
        @Json(name = "right_deliverables")
        RIGHT_DELIVERABLES("right_deliverables");

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
    public static JsonArray toJsonArray(WorkOrderRatingsAssignedProviderWorkOrderCategories[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderRatingsAssignedProviderWorkOrderCategories item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderRatingsAssignedProviderWorkOrderCategories[] fromJsonArray(JsonArray array) {
        WorkOrderRatingsAssignedProviderWorkOrderCategories[] list = new WorkOrderRatingsAssignedProviderWorkOrderCategories[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderRatingsAssignedProviderWorkOrderCategories fromJson(JsonObject obj) {
        try {
            return new WorkOrderRatingsAssignedProviderWorkOrderCategories(obj);
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
    public static final Parcelable.Creator<WorkOrderRatingsAssignedProviderWorkOrderCategories> CREATOR = new Parcelable.Creator<WorkOrderRatingsAssignedProviderWorkOrderCategories>() {

        @Override
        public WorkOrderRatingsAssignedProviderWorkOrderCategories createFromParcel(Parcel source) {
            try {
                return WorkOrderRatingsAssignedProviderWorkOrderCategories.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderRatingsAssignedProviderWorkOrderCategories[] newArray(int size) {
            return new WorkOrderRatingsAssignedProviderWorkOrderCategories[size];
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
        return true;
    }
}
