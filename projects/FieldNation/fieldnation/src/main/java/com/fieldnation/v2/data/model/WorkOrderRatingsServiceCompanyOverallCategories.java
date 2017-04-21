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

public class WorkOrderRatingsServiceCompanyOverallCategories implements Parcelable {
    private static final String TAG = "WorkOrderRatingsServiceCompanyOverallCategories";

    @Json(name = "percent")
    private Integer _percent;

    @Json(name = "type")
    private String _type;

    @Source
    private JsonObject SOURCE;

    public WorkOrderRatingsServiceCompanyOverallCategories() {
        SOURCE = new JsonObject();
    }

    public WorkOrderRatingsServiceCompanyOverallCategories(JsonObject obj) {
        SOURCE = obj;
    }

    public void setPercent(Integer percent) throws ParseException {
        _percent = percent;
        SOURCE.put("percent", percent);
    }

    public Integer getPercent() {
        try {
            if (_percent == null && SOURCE.has("percent") && SOURCE.get("percent") != null)
                _percent = SOURCE.getInt("percent");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _percent;
    }

    public WorkOrderRatingsServiceCompanyOverallCategories percent(Integer percent) throws ParseException {
        _percent = percent;
        SOURCE.put("percent", percent);
        return this;
    }

    public void setType(String type) throws ParseException {
        _type = type;
        SOURCE.put("type", type);
    }

    public String getType() {
        try {
            if (_type == null && SOURCE.has("type") && SOURCE.get("type") != null)
                _type = SOURCE.getString("type");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _type;
    }

    public WorkOrderRatingsServiceCompanyOverallCategories type(String type) throws ParseException {
        _type = type;
        SOURCE.put("type", type);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(WorkOrderRatingsServiceCompanyOverallCategories[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderRatingsServiceCompanyOverallCategories item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderRatingsServiceCompanyOverallCategories[] fromJsonArray(JsonArray array) {
        WorkOrderRatingsServiceCompanyOverallCategories[] list = new WorkOrderRatingsServiceCompanyOverallCategories[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderRatingsServiceCompanyOverallCategories fromJson(JsonObject obj) {
        try {
            return new WorkOrderRatingsServiceCompanyOverallCategories(obj);
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
    public static final Parcelable.Creator<WorkOrderRatingsServiceCompanyOverallCategories> CREATOR = new Parcelable.Creator<WorkOrderRatingsServiceCompanyOverallCategories>() {

        @Override
        public WorkOrderRatingsServiceCompanyOverallCategories createFromParcel(Parcel source) {
            try {
                return WorkOrderRatingsServiceCompanyOverallCategories.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderRatingsServiceCompanyOverallCategories[] newArray(int size) {
            return new WorkOrderRatingsServiceCompanyOverallCategories[size];
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
