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

public class WorkOrderRatingsServiceCompanyOverall implements Parcelable {
    private static final String TAG = "WorkOrderRatingsServiceCompanyOverall";

    @Json(name = "categories")
    private WorkOrderRatingsServiceCompanyOverallCategories[] _categories;

    @Json(name = "penalized")
    private Boolean _penalized;

    @Json(name = "ratings")
    private Integer _ratings;

    @Json(name = "stars")
    private Double _stars;

    @Source
    private JsonObject SOURCE;

    public WorkOrderRatingsServiceCompanyOverall() {
        SOURCE = new JsonObject();
    }

    public WorkOrderRatingsServiceCompanyOverall(JsonObject obj) {
        SOURCE = obj;
    }

    public void setCategories(WorkOrderRatingsServiceCompanyOverallCategories[] categories) throws ParseException {
        _categories = categories;
        SOURCE.put("categories", WorkOrderRatingsServiceCompanyOverallCategories.toJsonArray(categories));
    }

    public WorkOrderRatingsServiceCompanyOverallCategories[] getCategories() {
        try {
            if (_categories != null)
                return _categories;

            if (SOURCE.has("categories") && SOURCE.get("categories") != null) {
                _categories = WorkOrderRatingsServiceCompanyOverallCategories.fromJsonArray(SOURCE.getJsonArray("categories"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_categories == null)
            _categories = new WorkOrderRatingsServiceCompanyOverallCategories[0];

        return _categories;
    }

    public WorkOrderRatingsServiceCompanyOverall categories(WorkOrderRatingsServiceCompanyOverallCategories[] categories) throws ParseException {
        _categories = categories;
        SOURCE.put("categories", WorkOrderRatingsServiceCompanyOverallCategories.toJsonArray(categories), true);
        return this;
    }

    public void setPenalized(Boolean penalized) throws ParseException {
        _penalized = penalized;
        SOURCE.put("penalized", penalized);
    }

    public Boolean getPenalized() {
        try {
            if (_penalized == null && SOURCE.has("penalized") && SOURCE.get("penalized") != null)
                _penalized = SOURCE.getBoolean("penalized");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _penalized;
    }

    public WorkOrderRatingsServiceCompanyOverall penalized(Boolean penalized) throws ParseException {
        _penalized = penalized;
        SOURCE.put("penalized", penalized);
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

    public WorkOrderRatingsServiceCompanyOverall ratings(Integer ratings) throws ParseException {
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

    public WorkOrderRatingsServiceCompanyOverall stars(Double stars) throws ParseException {
        _stars = stars;
        SOURCE.put("stars", stars);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(WorkOrderRatingsServiceCompanyOverall[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderRatingsServiceCompanyOverall item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderRatingsServiceCompanyOverall[] fromJsonArray(JsonArray array) {
        WorkOrderRatingsServiceCompanyOverall[] list = new WorkOrderRatingsServiceCompanyOverall[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderRatingsServiceCompanyOverall fromJson(JsonObject obj) {
        try {
            return new WorkOrderRatingsServiceCompanyOverall(obj);
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
    public static final Parcelable.Creator<WorkOrderRatingsServiceCompanyOverall> CREATOR = new Parcelable.Creator<WorkOrderRatingsServiceCompanyOverall>() {

        @Override
        public WorkOrderRatingsServiceCompanyOverall createFromParcel(Parcel source) {
            try {
                return WorkOrderRatingsServiceCompanyOverall.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderRatingsServiceCompanyOverall[] newArray(int size) {
            return new WorkOrderRatingsServiceCompanyOverall[size];
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
