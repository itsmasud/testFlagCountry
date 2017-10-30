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

    @Source
    private JsonObject SOURCE;

    public RatingCompanyMarketplace() {
        SOURCE = new JsonObject();
    }

    public RatingCompanyMarketplace(JsonObject obj) {
        SOURCE = obj;
    }

    public void setAverageDaysToApproval(Integer averageDaysToApproval) throws ParseException {
        _averageDaysToApproval = averageDaysToApproval;
        SOURCE.put("average_days_to_approval", averageDaysToApproval);
    }

    public Integer getAverageDaysToApproval() {
        try {
            if (_averageDaysToApproval == null && SOURCE.has("average_days_to_approval") && SOURCE.get("average_days_to_approval") != null)
                _averageDaysToApproval = SOURCE.getInt("average_days_to_approval");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _averageDaysToApproval;
    }

    public RatingCompanyMarketplace averageDaysToApproval(Integer averageDaysToApproval) throws ParseException {
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
            if (_clearExpectations == null && SOURCE.has("clear_expectations") && SOURCE.get("clear_expectations") != null)
                _clearExpectations = SOURCE.getInt("clear_expectations");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _clearExpectations;
    }

    public RatingCompanyMarketplace clearExpectations(Integer clearExpectations) throws ParseException {
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
            if (_respectRating == null && SOURCE.has("respect_rating") && SOURCE.get("respect_rating") != null)
                _respectRating = SOURCE.getInt("respect_rating");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _respectRating;
    }

    public RatingCompanyMarketplace respectRating(Integer respectRating) throws ParseException {
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
            if (_stars == null && SOURCE.has("stars") && SOURCE.get("stars") != null)
                _stars = SOURCE.getDouble("stars");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _stars;
    }

    public RatingCompanyMarketplace stars(Double stars) throws ParseException {
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
            if (_totalRatings == null && SOURCE.has("total_ratings") && SOURCE.get("total_ratings") != null)
                _totalRatings = SOURCE.getInt("total_ratings");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _totalRatings;
    }

    public RatingCompanyMarketplace totalRatings(Integer totalRatings) throws ParseException {
        _totalRatings = totalRatings;
        SOURCE.put("total_ratings", totalRatings);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(RatingCompanyMarketplace[] array) {
        JsonArray list = new JsonArray();
        for (RatingCompanyMarketplace item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static RatingCompanyMarketplace[] fromJsonArray(JsonArray array) {
        RatingCompanyMarketplace[] list = new RatingCompanyMarketplace[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static RatingCompanyMarketplace fromJson(JsonObject obj) {
        try {
            return new RatingCompanyMarketplace(obj);
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
        dest.writeParcelable(getJson(), flags);
    }

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

}
