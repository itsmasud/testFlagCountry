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

public class RatingCompanyMine implements Parcelable {
    private static final String TAG = "RatingCompanyMine";

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

    @Source
    private JsonObject SOURCE;

    public RatingCompanyMine() {
        SOURCE = new JsonObject();
    }

    public RatingCompanyMine(JsonObject obj) {
        SOURCE = obj;
    }

    public void setAverageDaysToApproval(Integer averageDaysToApproval) throws ParseException {
        _averageDaysToApproval = averageDaysToApproval;
        SOURCE.put("average_days_to_approval", averageDaysToApproval);
    }

    public Integer getAverageDaysToApproval() {
        try {
            if (_averageDaysToApproval != null)
                return _averageDaysToApproval;

            if (SOURCE.has("average_days_to_approval") && SOURCE.get("average_days_to_approval") != null)
                _averageDaysToApproval = SOURCE.getInt("average_days_to_approval");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _averageDaysToApproval;
    }

    public RatingCompanyMine averageDaysToApproval(Integer averageDaysToApproval) throws ParseException {
        _averageDaysToApproval = averageDaysToApproval;
        SOURCE.put("average_days_to_approval", averageDaysToApproval);
        return this;
    }

    public void setClearExpectations(Integer clearExpectations) throws ParseException {
        _clearExpectations = clearExpectations;
        SOURCE.put("clear_expectations", clearExpectations);
    }

    public Integer getClearExpectations() {
        try {
            if (_clearExpectations != null)
                return _clearExpectations;

            if (SOURCE.has("clear_expectations") && SOURCE.get("clear_expectations") != null)
                _clearExpectations = SOURCE.getInt("clear_expectations");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _clearExpectations;
    }

    public RatingCompanyMine clearExpectations(Integer clearExpectations) throws ParseException {
        _clearExpectations = clearExpectations;
        SOURCE.put("clear_expectations", clearExpectations);
        return this;
    }

    public void setRespectRating(Integer respectRating) throws ParseException {
        _respectRating = respectRating;
        SOURCE.put("respect_rating", respectRating);
    }

    public Integer getRespectRating() {
        try {
            if (_respectRating != null)
                return _respectRating;

            if (SOURCE.has("respect_rating") && SOURCE.get("respect_rating") != null)
                _respectRating = SOURCE.getInt("respect_rating");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _respectRating;
    }

    public RatingCompanyMine respectRating(Integer respectRating) throws ParseException {
        _respectRating = respectRating;
        SOURCE.put("respect_rating", respectRating);
        return this;
    }

    public void setStars(Double stars) throws ParseException {
        _stars = stars;
        SOURCE.put("stars", stars);
    }

    public Double getStars() {
        try {
            if (_stars != null)
                return _stars;

            if (SOURCE.has("stars") && SOURCE.get("stars") != null)
                _stars = SOURCE.getDouble("stars");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _stars;
    }

    public RatingCompanyMine stars(Double stars) throws ParseException {
        _stars = stars;
        SOURCE.put("stars", stars);
        return this;
    }

    public void setTotalRatings(Integer totalRatings) throws ParseException {
        _totalRatings = totalRatings;
        SOURCE.put("total_ratings", totalRatings);
    }

    public Integer getTotalRatings() {
        try {
            if (_totalRatings != null)
                return _totalRatings;

            if (SOURCE.has("total_ratings") && SOURCE.get("total_ratings") != null)
                _totalRatings = SOURCE.getInt("total_ratings");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _totalRatings;
    }

    public RatingCompanyMine totalRatings(Integer totalRatings) throws ParseException {
        _totalRatings = totalRatings;
        SOURCE.put("total_ratings", totalRatings);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(RatingCompanyMine[] array) {
        JsonArray list = new JsonArray();
        for (RatingCompanyMine item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static RatingCompanyMine[] fromJsonArray(JsonArray array) {
        RatingCompanyMine[] list = new RatingCompanyMine[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static RatingCompanyMine fromJson(JsonObject obj) {
        try {
            return new RatingCompanyMine(obj);
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
    public static final Parcelable.Creator<RatingCompanyMine> CREATOR = new Parcelable.Creator<RatingCompanyMine>() {

        @Override
        public RatingCompanyMine createFromParcel(Parcel source) {
            try {
                return RatingCompanyMine.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public RatingCompanyMine[] newArray(int size) {
            return new RatingCompanyMine[size];
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
