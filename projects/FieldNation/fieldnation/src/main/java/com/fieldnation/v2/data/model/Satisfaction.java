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

public class Satisfaction implements Parcelable {
    private static final String TAG = "Satisfaction";

    @Json(name = "average_days_to_approval")
    private Integer _averageDaysToApproval;

    @Json(name = "clear_expectation")
    private Integer _clearExpectation;

    @Json(name = "respect_rating")
    private Integer _respectRating;

    @Json(name = "stars")
    private Double _stars;

    @Json(name = "total_ratings")
    private Integer _totalRatings;

    public Satisfaction() {
    }

    public void setAverageDaysToApproval(Integer averageDaysToApproval) {
        _averageDaysToApproval = averageDaysToApproval;
    }

    public Integer getAverageDaysToApproval() {
        return _averageDaysToApproval;
    }

    public Satisfaction averageDaysToApproval(Integer averageDaysToApproval) {
        _averageDaysToApproval = averageDaysToApproval;
        return this;
    }

    public void setClearExpectation(Integer clearExpectation) {
        _clearExpectation = clearExpectation;
    }

    public Integer getClearExpectation() {
        return _clearExpectation;
    }

    public Satisfaction clearExpectation(Integer clearExpectation) {
        _clearExpectation = clearExpectation;
        return this;
    }

    public void setRespectRating(Integer respectRating) {
        _respectRating = respectRating;
    }

    public Integer getRespectRating() {
        return _respectRating;
    }

    public Satisfaction respectRating(Integer respectRating) {
        _respectRating = respectRating;
        return this;
    }

    public void setStars(Double stars) {
        _stars = stars;
    }

    public Double getStars() {
        return _stars;
    }

    public Satisfaction stars(Double stars) {
        _stars = stars;
        return this;
    }

    public void setTotalRatings(Integer totalRatings) {
        _totalRatings = totalRatings;
    }

    public Integer getTotalRatings() {
        return _totalRatings;
    }

    public Satisfaction totalRatings(Integer totalRatings) {
        _totalRatings = totalRatings;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Satisfaction[] fromJsonArray(JsonArray array) {
        Satisfaction[] list = new Satisfaction[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Satisfaction fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Satisfaction.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Satisfaction satisfaction) {
        try {
            return Serializer.serializeObject(satisfaction);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Satisfaction> CREATOR = new Parcelable.Creator<Satisfaction>() {

        @Override
        public Satisfaction createFromParcel(Parcel source) {
            try {
                return Satisfaction.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Satisfaction[] newArray(int size) {
            return new Satisfaction[size];
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
