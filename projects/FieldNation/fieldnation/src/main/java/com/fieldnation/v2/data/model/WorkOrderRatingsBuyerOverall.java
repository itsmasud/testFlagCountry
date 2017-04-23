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

public class WorkOrderRatingsBuyerOverall implements Parcelable {
    private static final String TAG = "WorkOrderRatingsBuyerOverall";

    @Json(name = "approval_days")
    private Integer _approvalDays;

    @Json(name = "approval_period")
    private Integer _approvalPeriod;

    @Json(name = "jobs")
    private Integer _jobs;

    @Json(name = "percent_approval")
    private WorkOrderRatingsBuyerOverallPercentApproval[] _percentApproval;

    @Json(name = "percent_clear_expectations")
    private Integer _percentClearExpectations;

    @Json(name = "percent_respectful")
    private Integer _percentRespectful;

    @Json(name = "ratings")
    private Integer _ratings;

    @Json(name = "stars")
    private Double _stars;

    @Source
    private JsonObject SOURCE;

    public WorkOrderRatingsBuyerOverall() {
        SOURCE = new JsonObject();
    }

    public WorkOrderRatingsBuyerOverall(JsonObject obj) {
        SOURCE = obj;
    }

    public void setApprovalDays(Integer approvalDays) throws ParseException {
        _approvalDays = approvalDays;
        SOURCE.put("approval_days", approvalDays);
    }

    public Integer getApprovalDays() {
        try {
            if (_approvalDays == null && SOURCE.has("approval_days") && SOURCE.get("approval_days") != null)
                _approvalDays = SOURCE.getInt("approval_days");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _approvalDays;
    }

    public WorkOrderRatingsBuyerOverall approvalDays(Integer approvalDays) throws ParseException {
        _approvalDays = approvalDays;
        SOURCE.put("approval_days", approvalDays);
        return this;
    }

    public void setApprovalPeriod(Integer approvalPeriod) throws ParseException {
        _approvalPeriod = approvalPeriod;
        SOURCE.put("approval_period", approvalPeriod);
    }

    public Integer getApprovalPeriod() {
        try {
            if (_approvalPeriod == null && SOURCE.has("approval_period") && SOURCE.get("approval_period") != null)
                _approvalPeriod = SOURCE.getInt("approval_period");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _approvalPeriod;
    }

    public WorkOrderRatingsBuyerOverall approvalPeriod(Integer approvalPeriod) throws ParseException {
        _approvalPeriod = approvalPeriod;
        SOURCE.put("approval_period", approvalPeriod);
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

    public WorkOrderRatingsBuyerOverall jobs(Integer jobs) throws ParseException {
        _jobs = jobs;
        SOURCE.put("jobs", jobs);
        return this;
    }

    public void setPercentApproval(WorkOrderRatingsBuyerOverallPercentApproval[] percentApproval) throws ParseException {
        _percentApproval = percentApproval;
        SOURCE.put("percent_approval", WorkOrderRatingsBuyerOverallPercentApproval.toJsonArray(percentApproval));
    }

    public WorkOrderRatingsBuyerOverallPercentApproval[] getPercentApproval() {
        try {
            if (_percentApproval != null)
                return _percentApproval;

            if (SOURCE.has("percent_approval") && SOURCE.get("percent_approval") != null) {
                _percentApproval = WorkOrderRatingsBuyerOverallPercentApproval.fromJsonArray(SOURCE.getJsonArray("percent_approval"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _percentApproval;
    }

    public WorkOrderRatingsBuyerOverall percentApproval(WorkOrderRatingsBuyerOverallPercentApproval[] percentApproval) throws ParseException {
        _percentApproval = percentApproval;
        SOURCE.put("percent_approval", WorkOrderRatingsBuyerOverallPercentApproval.toJsonArray(percentApproval), true);
        return this;
    }

    public void setPercentClearExpectations(Integer percentClearExpectations) throws ParseException {
        _percentClearExpectations = percentClearExpectations;
        SOURCE.put("percent_clear_expectations", percentClearExpectations);
    }

    public Integer getPercentClearExpectations() {
        try {
            if (_percentClearExpectations == null && SOURCE.has("percent_clear_expectations") && SOURCE.get("percent_clear_expectations") != null)
                _percentClearExpectations = SOURCE.getInt("percent_clear_expectations");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _percentClearExpectations;
    }

    public WorkOrderRatingsBuyerOverall percentClearExpectations(Integer percentClearExpectations) throws ParseException {
        _percentClearExpectations = percentClearExpectations;
        SOURCE.put("percent_clear_expectations", percentClearExpectations);
        return this;
    }

    public void setPercentRespectful(Integer percentRespectful) throws ParseException {
        _percentRespectful = percentRespectful;
        SOURCE.put("percent_respectful", percentRespectful);
    }

    public Integer getPercentRespectful() {
        try {
            if (_percentRespectful == null && SOURCE.has("percent_respectful") && SOURCE.get("percent_respectful") != null)
                _percentRespectful = SOURCE.getInt("percent_respectful");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _percentRespectful;
    }

    public WorkOrderRatingsBuyerOverall percentRespectful(Integer percentRespectful) throws ParseException {
        _percentRespectful = percentRespectful;
        SOURCE.put("percent_respectful", percentRespectful);
        return this;
    }

    public void setRatings(Integer ratings) throws ParseException {
        _ratings = ratings;
        SOURCE.put("ratings", ratings);
    }

    public Integer getRatings() {
        try {
            if (_ratings == null && SOURCE.has("ratings") && SOURCE.get("ratings") != null)
                _ratings = SOURCE.getInt("ratings");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _ratings;
    }

    public WorkOrderRatingsBuyerOverall ratings(Integer ratings) throws ParseException {
        _ratings = ratings;
        SOURCE.put("ratings", ratings);
        return this;
    }

    public void setStars(Double stars) throws ParseException {
        _stars = stars;
        SOURCE.put("stars", stars);
    }

    public Double getStars() {
        try {
            if (_stars == null && SOURCE.has("stars") && SOURCE.get("stars") != null)
                _stars = SOURCE.getDouble("stars");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _stars;
    }

    public WorkOrderRatingsBuyerOverall stars(Double stars) throws ParseException {
        _stars = stars;
        SOURCE.put("stars", stars);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(WorkOrderRatingsBuyerOverall[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderRatingsBuyerOverall item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderRatingsBuyerOverall[] fromJsonArray(JsonArray array) {
        WorkOrderRatingsBuyerOverall[] list = new WorkOrderRatingsBuyerOverall[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderRatingsBuyerOverall fromJson(JsonObject obj) {
        try {
            return new WorkOrderRatingsBuyerOverall(obj);
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
    public static final Parcelable.Creator<WorkOrderRatingsBuyerOverall> CREATOR = new Parcelable.Creator<WorkOrderRatingsBuyerOverall>() {

        @Override
        public WorkOrderRatingsBuyerOverall createFromParcel(Parcel source) {
            try {
                return WorkOrderRatingsBuyerOverall.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderRatingsBuyerOverall[] newArray(int size) {
            return new WorkOrderRatingsBuyerOverall[size];
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
