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

public class UserRatingMyCompany implements Parcelable {
    private static final String TAG = "UserRatingMyCompany";

    @Json(name = "total_ratings")
    private Integer _totalRatings;

    @Json(name = "assignment_fulfilled_percent")
    private Integer _assignmentFulfilledPercent;

    @Json(name = "stars")
    private Double _stars;

    @Json(name = "on_time_percent")
    private Integer _onTimePercent;

    @Json(name = "right_deliverables_percent")
    private Integer _rightDeliverablesPercent;

    public UserRatingMyCompany() {
    }

    public void setTotalRatings(Integer totalRatings) {
        _totalRatings = totalRatings;
    }

    public Integer getTotalRatings() {
        return _totalRatings;
    }

    public UserRatingMyCompany totalRatings(Integer totalRatings) {
        _totalRatings = totalRatings;
        return this;
    }

    public void setAssignmentFulfilledPercent(Integer assignmentFulfilledPercent) {
        _assignmentFulfilledPercent = assignmentFulfilledPercent;
    }

    public Integer getAssignmentFulfilledPercent() {
        return _assignmentFulfilledPercent;
    }

    public UserRatingMyCompany assignmentFulfilledPercent(Integer assignmentFulfilledPercent) {
        _assignmentFulfilledPercent = assignmentFulfilledPercent;
        return this;
    }

    public void setStars(Double stars) {
        _stars = stars;
    }

    public Double getStars() {
        return _stars;
    }

    public UserRatingMyCompany stars(Double stars) {
        _stars = stars;
        return this;
    }

    public void setOnTimePercent(Integer onTimePercent) {
        _onTimePercent = onTimePercent;
    }

    public Integer getOnTimePercent() {
        return _onTimePercent;
    }

    public UserRatingMyCompany onTimePercent(Integer onTimePercent) {
        _onTimePercent = onTimePercent;
        return this;
    }

    public void setRightDeliverablesPercent(Integer rightDeliverablesPercent) {
        _rightDeliverablesPercent = rightDeliverablesPercent;
    }

    public Integer getRightDeliverablesPercent() {
        return _rightDeliverablesPercent;
    }

    public UserRatingMyCompany rightDeliverablesPercent(Integer rightDeliverablesPercent) {
        _rightDeliverablesPercent = rightDeliverablesPercent;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UserRatingMyCompany[] fromJsonArray(JsonArray array) {
        UserRatingMyCompany[] list = new UserRatingMyCompany[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UserRatingMyCompany fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UserRatingMyCompany.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UserRatingMyCompany userRatingMyCompany) {
        try {
            return Serializer.serializeObject(userRatingMyCompany);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<UserRatingMyCompany> CREATOR = new Parcelable.Creator<UserRatingMyCompany>() {

        @Override
        public UserRatingMyCompany createFromParcel(Parcel source) {
            try {
                return UserRatingMyCompany.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UserRatingMyCompany[] newArray(int size) {
            return new UserRatingMyCompany[size];
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
