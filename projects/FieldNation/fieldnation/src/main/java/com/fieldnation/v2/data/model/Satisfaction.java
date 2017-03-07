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

    @Source
    private JsonObject SOURCE;

    public Satisfaction() {
        SOURCE = new JsonObject();
    }

    public Satisfaction(JsonObject obj) {
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

    public Satisfaction averageDaysToApproval(Integer averageDaysToApproval) throws ParseException {
        _averageDaysToApproval = averageDaysToApproval;
        SOURCE.put("average_days_to_approval", averageDaysToApproval);
        return this;
    }

    public void setClearExpectation(Integer clearExpectation) throws ParseException {
        _clearExpectation = clearExpectation;
        SOURCE.put("clear_expectation", clearExpectation);
    }

    public Integer getClearExpectation() {
        try {
            if (_clearExpectation != null)
                return _clearExpectation;

            if (SOURCE.has("clear_expectation") && SOURCE.get("clear_expectation") != null)
                _clearExpectation = SOURCE.getInt("clear_expectation");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _clearExpectation;
    }

    public Satisfaction clearExpectation(Integer clearExpectation) throws ParseException {
        _clearExpectation = clearExpectation;
        SOURCE.put("clear_expectation", clearExpectation);
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

    public Satisfaction respectRating(Integer respectRating) throws ParseException {
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

    public Satisfaction stars(Double stars) throws ParseException {
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

    public Satisfaction totalRatings(Integer totalRatings) throws ParseException {
        _totalRatings = totalRatings;
        SOURCE.put("total_ratings", totalRatings);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Satisfaction[] array) {
        JsonArray list = new JsonArray();
        for (Satisfaction item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Satisfaction[] fromJsonArray(JsonArray array) {
        Satisfaction[] list = new Satisfaction[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Satisfaction fromJson(JsonObject obj) {
        try {
            return new Satisfaction(obj);
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
        dest.writeParcelable(getJson(), flags);
    }
}
