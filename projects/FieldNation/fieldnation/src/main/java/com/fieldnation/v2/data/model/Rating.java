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

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class Rating implements Parcelable {
    private static final String TAG = "Rating";

    @Json(name = "assignment_fulfilled_percent")
    private Double _assignmentFulfilledPercent;

    @Json(name = "company")
    private RatingCompany _company;

    @Json(name = "follow_instructions_percent")
    private Double _followInstructionsPercent;

    @Json(name = "on_time_percent")
    private Double _onTimePercent;

    @Json(name = "right_deliverables_percent")
    private Double _rightDeliverablesPercent;

    @Json(name = "stars")
    private Double _stars;

    @Json(name = "total_ratings")
    private Integer _totalRatings;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public Rating() {
    }

    public void setAssignmentFulfilledPercent(Double assignmentFulfilledPercent) throws ParseException {
        _assignmentFulfilledPercent = assignmentFulfilledPercent;
        SOURCE.put("assignment_fulfilled_percent", assignmentFulfilledPercent);
    }

    public Double getAssignmentFulfilledPercent() {
        return _assignmentFulfilledPercent;
    }

    public Rating assignmentFulfilledPercent(Double assignmentFulfilledPercent) throws ParseException {
        _assignmentFulfilledPercent = assignmentFulfilledPercent;
        SOURCE.put("assignment_fulfilled_percent", assignmentFulfilledPercent);
        return this;
    }

    public void setCompany(RatingCompany company) throws ParseException {
        _company = company;
        SOURCE.put("company", company.getJson());
    }

    public RatingCompany getCompany() {
        return _company;
    }

    public Rating company(RatingCompany company) throws ParseException {
        _company = company;
        SOURCE.put("company", company.getJson());
        return this;
    }

    public void setFollowInstructionsPercent(Double followInstructionsPercent) throws ParseException {
        _followInstructionsPercent = followInstructionsPercent;
        SOURCE.put("follow_instructions_percent", followInstructionsPercent);
    }

    public Double getFollowInstructionsPercent() {
        return _followInstructionsPercent;
    }

    public Rating followInstructionsPercent(Double followInstructionsPercent) throws ParseException {
        _followInstructionsPercent = followInstructionsPercent;
        SOURCE.put("follow_instructions_percent", followInstructionsPercent);
        return this;
    }

    public void setOnTimePercent(Double onTimePercent) throws ParseException {
        _onTimePercent = onTimePercent;
        SOURCE.put("on_time_percent", onTimePercent);
    }

    public Double getOnTimePercent() {
        return _onTimePercent;
    }

    public Rating onTimePercent(Double onTimePercent) throws ParseException {
        _onTimePercent = onTimePercent;
        SOURCE.put("on_time_percent", onTimePercent);
        return this;
    }

    public void setRightDeliverablesPercent(Double rightDeliverablesPercent) throws ParseException {
        _rightDeliverablesPercent = rightDeliverablesPercent;
        SOURCE.put("right_deliverables_percent", rightDeliverablesPercent);
    }

    public Double getRightDeliverablesPercent() {
        return _rightDeliverablesPercent;
    }

    public Rating rightDeliverablesPercent(Double rightDeliverablesPercent) throws ParseException {
        _rightDeliverablesPercent = rightDeliverablesPercent;
        SOURCE.put("right_deliverables_percent", rightDeliverablesPercent);
        return this;
    }

    public void setStars(Double stars) throws ParseException {
        _stars = stars;
        SOURCE.put("stars", stars);
    }

    public Double getStars() {
        return _stars;
    }

    public Rating stars(Double stars) throws ParseException {
        _stars = stars;
        SOURCE.put("stars", stars);
        return this;
    }

    public void setTotalRatings(Integer totalRatings) throws ParseException {
        _totalRatings = totalRatings;
        SOURCE.put("total_ratings", totalRatings);
    }

    public Integer getTotalRatings() {
        return _totalRatings;
    }

    public Rating totalRatings(Integer totalRatings) throws ParseException {
        _totalRatings = totalRatings;
        SOURCE.put("total_ratings", totalRatings);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Rating[] array) {
        JsonArray list = new JsonArray();
        for (Rating item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Rating[] fromJsonArray(JsonArray array) {
        Rating[] list = new Rating[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Rating fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Rating.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Rating> CREATOR = new Parcelable.Creator<Rating>() {

        @Override
        public Rating createFromParcel(Parcel source) {
            try {
                return Rating.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Rating[] newArray(int size) {
            return new Rating[size];
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
}
