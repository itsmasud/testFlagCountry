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

public class ProviderRatings implements Parcelable {
    private static final String TAG = "ProviderRatings";

    @Json(name = "categories")
    private ProviderRatingsCategories[] _categories;

    @Json(name = "ratings")
    private Integer _ratings;

    @Json(name = "stars")
    private Double _stars;

    @Source
    private JsonObject SOURCE;

    public ProviderRatings() {
        SOURCE = new JsonObject();
    }

    public ProviderRatings(JsonObject obj) {
        SOURCE = obj;
    }

    public void setCategories(ProviderRatingsCategories[] categories) throws ParseException {
        _categories = categories;
        SOURCE.put("categories", ProviderRatingsCategories.toJsonArray(categories));
    }

    public ProviderRatingsCategories[] getCategories() {
        try {
            if (_categories != null)
                return _categories;

            if (SOURCE.has("categories") && SOURCE.get("categories") != null) {
                _categories = ProviderRatingsCategories.fromJsonArray(SOURCE.getJsonArray("categories"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _categories;
    }

    public ProviderRatings categories(ProviderRatingsCategories[] categories) throws ParseException {
        _categories = categories;
        SOURCE.put("categories", ProviderRatingsCategories.toJsonArray(categories), true);
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

    public ProviderRatings ratings(Integer ratings) throws ParseException {
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

    public ProviderRatings stars(Double stars) throws ParseException {
        _stars = stars;
        SOURCE.put("stars", stars);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(ProviderRatings[] array) {
        JsonArray list = new JsonArray();
        for (ProviderRatings item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static ProviderRatings[] fromJsonArray(JsonArray array) {
        ProviderRatings[] list = new ProviderRatings[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ProviderRatings fromJson(JsonObject obj) {
        try {
            return new ProviderRatings(obj);
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
    public static final Parcelable.Creator<ProviderRatings> CREATOR = new Parcelable.Creator<ProviderRatings>() {

        @Override
        public ProviderRatings createFromParcel(Parcel source) {
            try {
                return ProviderRatings.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ProviderRatings[] newArray(int size) {
            return new ProviderRatings[size];
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
