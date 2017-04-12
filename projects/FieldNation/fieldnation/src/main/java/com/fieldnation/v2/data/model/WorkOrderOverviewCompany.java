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

public class WorkOrderOverviewCompany implements Parcelable {
    private static final String TAG = "WorkOrderOverviewCompany";

    @Json(name = "active_jobs")
    private Integer _activeJobs;

    @Json(name = "average_duration")
    private Integer _averageDuration;

    @Json(name = "cancelled_jobs")
    private Integer _cancelledJobs;

    @Json(name = "jobs")
    private Integer _jobs;

    @Json(name = "paid")
    private Double _paid;

    @Json(name = "percent_counters")
    private Double _percentCounters;

    @Json(name = "percent_expenses")
    private Double _percentExpenses;

    @Source
    private JsonObject SOURCE;

    public WorkOrderOverviewCompany() {
        SOURCE = new JsonObject();
    }

    public WorkOrderOverviewCompany(JsonObject obj) {
        SOURCE = obj;
    }

    public void setActiveJobs(Integer activeJobs) throws ParseException {
        _activeJobs = activeJobs;
        SOURCE.put("active_jobs", activeJobs);
    }

    public Integer getActiveJobs() {
        try {
            if (_activeJobs == null && SOURCE.has("active_jobs") && SOURCE.get("active_jobs") != null)
                _activeJobs = SOURCE.getInt("active_jobs");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _activeJobs;
    }

    public WorkOrderOverviewCompany activeJobs(Integer activeJobs) throws ParseException {
        _activeJobs = activeJobs;
        SOURCE.put("active_jobs", activeJobs);
        return this;
    }

    public void setAverageDuration(Integer averageDuration) throws ParseException {
        _averageDuration = averageDuration;
        SOURCE.put("average_duration", averageDuration);
    }

    public Integer getAverageDuration() {
        try {
            if (_averageDuration == null && SOURCE.has("average_duration") && SOURCE.get("average_duration") != null)
                _averageDuration = SOURCE.getInt("average_duration");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _averageDuration;
    }

    public WorkOrderOverviewCompany averageDuration(Integer averageDuration) throws ParseException {
        _averageDuration = averageDuration;
        SOURCE.put("average_duration", averageDuration);
        return this;
    }

    public void setCancelledJobs(Integer cancelledJobs) throws ParseException {
        _cancelledJobs = cancelledJobs;
        SOURCE.put("cancelled_jobs", cancelledJobs);
    }

    public Integer getCancelledJobs() {
        try {
            if (_cancelledJobs == null && SOURCE.has("cancelled_jobs") && SOURCE.get("cancelled_jobs") != null)
                _cancelledJobs = SOURCE.getInt("cancelled_jobs");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _cancelledJobs;
    }

    public WorkOrderOverviewCompany cancelledJobs(Integer cancelledJobs) throws ParseException {
        _cancelledJobs = cancelledJobs;
        SOURCE.put("cancelled_jobs", cancelledJobs);
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

    public WorkOrderOverviewCompany jobs(Integer jobs) throws ParseException {
        _jobs = jobs;
        SOURCE.put("jobs", jobs);
        return this;
    }

    public void setPaid(Double paid) throws ParseException {
        _paid = paid;
        SOURCE.put("paid", paid);
    }

    public Double getPaid() {
        try {
            if (_paid == null && SOURCE.has("paid") && SOURCE.get("paid") != null)
                _paid = SOURCE.getDouble("paid");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _paid;
    }

    public WorkOrderOverviewCompany paid(Double paid) throws ParseException {
        _paid = paid;
        SOURCE.put("paid", paid);
        return this;
    }

    public void setPercentCounters(Double percentCounters) throws ParseException {
        _percentCounters = percentCounters;
        SOURCE.put("percent_counters", percentCounters);
    }

    public Double getPercentCounters() {
        try {
            if (_percentCounters == null && SOURCE.has("percent_counters") && SOURCE.get("percent_counters") != null)
                _percentCounters = SOURCE.getDouble("percent_counters");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _percentCounters;
    }

    public WorkOrderOverviewCompany percentCounters(Double percentCounters) throws ParseException {
        _percentCounters = percentCounters;
        SOURCE.put("percent_counters", percentCounters);
        return this;
    }

    public void setPercentExpenses(Double percentExpenses) throws ParseException {
        _percentExpenses = percentExpenses;
        SOURCE.put("percent_expenses", percentExpenses);
    }

    public Double getPercentExpenses() {
        try {
            if (_percentExpenses == null && SOURCE.has("percent_expenses") && SOURCE.get("percent_expenses") != null)
                _percentExpenses = SOURCE.getDouble("percent_expenses");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _percentExpenses;
    }

    public WorkOrderOverviewCompany percentExpenses(Double percentExpenses) throws ParseException {
        _percentExpenses = percentExpenses;
        SOURCE.put("percent_expenses", percentExpenses);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(WorkOrderOverviewCompany[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderOverviewCompany item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderOverviewCompany[] fromJsonArray(JsonArray array) {
        WorkOrderOverviewCompany[] list = new WorkOrderOverviewCompany[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderOverviewCompany fromJson(JsonObject obj) {
        try {
            return new WorkOrderOverviewCompany(obj);
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
    public static final Parcelable.Creator<WorkOrderOverviewCompany> CREATOR = new Parcelable.Creator<WorkOrderOverviewCompany>() {

        @Override
        public WorkOrderOverviewCompany createFromParcel(Parcel source) {
            try {
                return WorkOrderOverviewCompany.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderOverviewCompany[] newArray(int size) {
            return new WorkOrderOverviewCompany[size];
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
