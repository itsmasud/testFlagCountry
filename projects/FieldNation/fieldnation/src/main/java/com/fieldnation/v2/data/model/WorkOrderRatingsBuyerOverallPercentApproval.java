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

public class WorkOrderRatingsBuyerOverallPercentApproval implements Parcelable {
    private static final String TAG = "WorkOrderRatingsBuyerOverallPercentApproval";

    @Json(name = "days")
    private Integer _days;

    @Json(name = "percent")
    private Integer _percent;

    @Source
    private JsonObject SOURCE;

    public WorkOrderRatingsBuyerOverallPercentApproval() {
        SOURCE = new JsonObject();
    }

    public WorkOrderRatingsBuyerOverallPercentApproval(JsonObject obj) {
        SOURCE = obj;
    }

    public void setDays(Integer days) throws ParseException {
        _days = days;
        SOURCE.put("days", days);
    }

    public Integer getDays() {
        try {
            if (_days == null && SOURCE.has("days") && SOURCE.get("days") != null)
                _days = SOURCE.getInt("days");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _days;
    }

    public WorkOrderRatingsBuyerOverallPercentApproval days(Integer days) throws ParseException {
        _days = days;
        SOURCE.put("days", days);
        return this;
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

    public WorkOrderRatingsBuyerOverallPercentApproval percent(Integer percent) throws ParseException {
        _percent = percent;
        SOURCE.put("percent", percent);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(WorkOrderRatingsBuyerOverallPercentApproval[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderRatingsBuyerOverallPercentApproval item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderRatingsBuyerOverallPercentApproval[] fromJsonArray(JsonArray array) {
        WorkOrderRatingsBuyerOverallPercentApproval[] list = new WorkOrderRatingsBuyerOverallPercentApproval[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderRatingsBuyerOverallPercentApproval fromJson(JsonObject obj) {
        try {
            return new WorkOrderRatingsBuyerOverallPercentApproval(obj);
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
    public static final Parcelable.Creator<WorkOrderRatingsBuyerOverallPercentApproval> CREATOR = new Parcelable.Creator<WorkOrderRatingsBuyerOverallPercentApproval>() {

        @Override
        public WorkOrderRatingsBuyerOverallPercentApproval createFromParcel(Parcel source) {
            try {
                return WorkOrderRatingsBuyerOverallPercentApproval.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderRatingsBuyerOverallPercentApproval[] newArray(int size) {
            return new WorkOrderRatingsBuyerOverallPercentApproval[size];
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
