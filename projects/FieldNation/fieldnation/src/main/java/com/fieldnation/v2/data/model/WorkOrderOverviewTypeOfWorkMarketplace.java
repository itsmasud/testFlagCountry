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

public class WorkOrderOverviewTypeOfWorkMarketplace implements Parcelable {
    private static final String TAG = "WorkOrderOverviewTypeOfWorkMarketplace";

    @Json(name = "average_duration")
    private Double _averageDuration;

    @Json(name = "average_hourly_rate")
    private Double _averageHourlyRate;

    @Source
    private JsonObject SOURCE;

    public WorkOrderOverviewTypeOfWorkMarketplace() {
        SOURCE = new JsonObject();
    }

    public WorkOrderOverviewTypeOfWorkMarketplace(JsonObject obj) {
        SOURCE = obj;
    }

    public void setAverageDuration(Double averageDuration) throws ParseException {
        _averageDuration = averageDuration;
        SOURCE.put("average_duration", averageDuration);
    }

    public Double getAverageDuration() {
        try {
            if (_averageDuration == null && SOURCE.has("average_duration") && SOURCE.get("average_duration") != null)
                _averageDuration = SOURCE.getDouble("average_duration");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _averageDuration;
    }

    public WorkOrderOverviewTypeOfWorkMarketplace averageDuration(Double averageDuration) throws ParseException {
        _averageDuration = averageDuration;
        SOURCE.put("average_duration", averageDuration);
        return this;
    }

    public void setAverageHourlyRate(Double averageHourlyRate) throws ParseException {
        _averageHourlyRate = averageHourlyRate;
        SOURCE.put("average_hourly_rate", averageHourlyRate);
    }

    public Double getAverageHourlyRate() {
        try {
            if (_averageHourlyRate == null && SOURCE.has("average_hourly_rate") && SOURCE.get("average_hourly_rate") != null)
                _averageHourlyRate = SOURCE.getDouble("average_hourly_rate");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _averageHourlyRate;
    }

    public WorkOrderOverviewTypeOfWorkMarketplace averageHourlyRate(Double averageHourlyRate) throws ParseException {
        _averageHourlyRate = averageHourlyRate;
        SOURCE.put("average_hourly_rate", averageHourlyRate);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(WorkOrderOverviewTypeOfWorkMarketplace[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderOverviewTypeOfWorkMarketplace item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderOverviewTypeOfWorkMarketplace[] fromJsonArray(JsonArray array) {
        WorkOrderOverviewTypeOfWorkMarketplace[] list = new WorkOrderOverviewTypeOfWorkMarketplace[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderOverviewTypeOfWorkMarketplace fromJson(JsonObject obj) {
        try {
            return new WorkOrderOverviewTypeOfWorkMarketplace(obj);
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
    public static final Parcelable.Creator<WorkOrderOverviewTypeOfWorkMarketplace> CREATOR = new Parcelable.Creator<WorkOrderOverviewTypeOfWorkMarketplace>() {

        @Override
        public WorkOrderOverviewTypeOfWorkMarketplace createFromParcel(Parcel source) {
            try {
                return WorkOrderOverviewTypeOfWorkMarketplace.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderOverviewTypeOfWorkMarketplace[] newArray(int size) {
            return new WorkOrderOverviewTypeOfWorkMarketplace[size];
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
