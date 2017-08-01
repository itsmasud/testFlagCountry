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

public class WorkOrderOverviewTypeOfWorkCompany implements Parcelable {
    private static final String TAG = "WorkOrderOverviewTypeOfWorkCompany";

    @Json(name = "average_duration")
    private Double _averageDuration;

    @Json(name = "average_hourly_rate")
    private Double _averageHourlyRate;

    @Json(name = "jobs")
    private Integer _jobs;

    @Source
    private JsonObject SOURCE;

    public WorkOrderOverviewTypeOfWorkCompany() {
        SOURCE = new JsonObject();
    }

    public WorkOrderOverviewTypeOfWorkCompany(JsonObject obj) {
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

    public WorkOrderOverviewTypeOfWorkCompany averageDuration(Double averageDuration) throws ParseException {
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

    public WorkOrderOverviewTypeOfWorkCompany averageHourlyRate(Double averageHourlyRate) throws ParseException {
        _averageHourlyRate = averageHourlyRate;
        SOURCE.put("average_hourly_rate", averageHourlyRate);
        return this;
    }

    public void setJobs(Integer jobs) throws ParseException {
        _jobs = jobs;
        SOURCE.put("jobs", jobs);
    }

    public Integer getJobs() {
        try {
            if (_jobs == null && SOURCE.has("jobs") && SOURCE.get("jobs") != null)
                _jobs = SOURCE.getInt("jobs");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _jobs;
    }

    public WorkOrderOverviewTypeOfWorkCompany jobs(Integer jobs) throws ParseException {
        _jobs = jobs;
        SOURCE.put("jobs", jobs);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(WorkOrderOverviewTypeOfWorkCompany[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderOverviewTypeOfWorkCompany item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderOverviewTypeOfWorkCompany[] fromJsonArray(JsonArray array) {
        WorkOrderOverviewTypeOfWorkCompany[] list = new WorkOrderOverviewTypeOfWorkCompany[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderOverviewTypeOfWorkCompany fromJson(JsonObject obj) {
        try {
            return new WorkOrderOverviewTypeOfWorkCompany(obj);
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
    public static final Parcelable.Creator<WorkOrderOverviewTypeOfWorkCompany> CREATOR = new Parcelable.Creator<WorkOrderOverviewTypeOfWorkCompany>() {

        @Override
        public WorkOrderOverviewTypeOfWorkCompany createFromParcel(Parcel source) {
            try {
                return WorkOrderOverviewTypeOfWorkCompany.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderOverviewTypeOfWorkCompany[] newArray(int size) {
            return new WorkOrderOverviewTypeOfWorkCompany[size];
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
