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

public class RatingCompanyMarketplace implements Parcelable {
    private static final String TAG = "RatingCompanyMarketplace";

    @Json(name = "average_days_to_approval")
    private Integer _averageDaysToApproval;

    @Json(name = "clear_expectations")
    private Integer _clearExpectations;

    @Json(name = "respect_rating")
    private Integer _respectRating;

    @Json(name = "stars")
    private Double _stars;

    @Json(name = "total_ratings")
    private Integer _totalRatings;

    public RatingCompanyMarketplace() {
    }

    public void setAverageDaysToApproval(Integer averageDaysToApproval) {
        _averageDaysToApproval = averageDaysToApproval;
    }

    public Integer getAverageDaysToApproval() {
        return _averageDaysToApproval;
    }

    public RatingCompanyMarketplace averageDaysToApproval(Integer averageDaysToApproval) {
        _averageDaysToApproval = averageDaysToApproval;
        return this;
    }

    public void setClearExpectations(Integer clearExpectations) {
        _clearExpectations = clearExpectations;
    }

    public Integer getClearExpectations() {
        return _clearExpectations;
    }

    public RatingCompanyMarketplace clearExpectations(Integer clearExpectations) {
        _clearExpectations = clearExpectations;
        return this;
    }

    public void setRespectRating(Integer respectRating) {
        _respectRating = respectRating;
    }

    public Integer getRespectRating() {
        return _respectRating;
    }

    public RatingCompanyMarketplace respectRating(Integer respectRating) {
        _respectRating = respectRating;
        return this;
    }

    public void setStars(Double stars) {
        _stars = stars;
    }

    public Double getStars() {
        return _stars;
    }

    public RatingCompanyMarketplace stars(Double stars) {
        _stars = stars;
        return this;
    }

    public void setTotalRatings(Integer totalRatings) {
        _totalRatings = totalRatings;
    }

    public Integer getTotalRatings() {
        return _totalRatings;
    }

    public RatingCompanyMarketplace totalRatings(Integer totalRatings) {
        _totalRatings = totalRatings;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static RatingCompanyMarketplace[] fromJsonArray(JsonArray array) {
        RatingCompanyMarketplace[] list = new RatingCompanyMarketplace[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static RatingCompanyMarketplace fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(RatingCompanyMarketplace.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(RatingCompanyMarketplace ratingCompanyMarketplace) {
        try {
            return Serializer.serializeObject(ratingCompanyMarketplace);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<RatingCompanyMarketplace> CREATOR = new Parcelable.Creator<RatingCompanyMarketplace>() {

        @Override
        public RatingCompanyMarketplace createFromParcel(Parcel source) {
            try {
                return RatingCompanyMarketplace.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public RatingCompanyMarketplace[] newArray(int size) {
            return new RatingCompanyMarketplace[size];
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
