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

public class CompanyRatingRating implements Parcelable {
    private static final String TAG = "CompanyRatingRating";

    @Json(name = "marketplace")
    private Satisfaction _marketplace;

    @Json(name = "mine")
    private Satisfaction _mine;

    @Json(name = "toggle_visible")
    private Boolean _toggleVisible;

    @Source
    private JsonObject SOURCE;

    public CompanyRatingRating() {
        SOURCE = new JsonObject();
    }

    public CompanyRatingRating(JsonObject obj) {
        SOURCE = obj;
    }

    public void setMarketplace(Satisfaction marketplace) throws ParseException {
        _marketplace = marketplace;
        SOURCE.put("marketplace", marketplace.getJson());
    }

    public Satisfaction getMarketplace() {
        try {
            if (_marketplace == null && SOURCE.has("marketplace") && SOURCE.get("marketplace") != null)
                _marketplace = Satisfaction.fromJson(SOURCE.getJsonObject("marketplace"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_marketplace != null && _marketplace.isSet())
        return _marketplace;

        return null;
    }

    public CompanyRatingRating marketplace(Satisfaction marketplace) throws ParseException {
        _marketplace = marketplace;
        SOURCE.put("marketplace", marketplace.getJson());
        return this;
    }

    public void setMine(Satisfaction mine) throws ParseException {
        _mine = mine;
        SOURCE.put("mine", mine.getJson());
    }

    public Satisfaction getMine() {
        try {
            if (_mine == null && SOURCE.has("mine") && SOURCE.get("mine") != null)
                _mine = Satisfaction.fromJson(SOURCE.getJsonObject("mine"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_mine != null && _mine.isSet())
        return _mine;

        return null;
    }

    public CompanyRatingRating mine(Satisfaction mine) throws ParseException {
        _mine = mine;
        SOURCE.put("mine", mine.getJson());
        return this;
    }

    public void setToggleVisible(Boolean toggleVisible) throws ParseException {
        _toggleVisible = toggleVisible;
        SOURCE.put("toggle_visible", toggleVisible);
    }

    public Boolean getToggleVisible() {
        try {
            if (_toggleVisible == null && SOURCE.has("toggle_visible") && SOURCE.get("toggle_visible") != null)
                _toggleVisible = SOURCE.getBoolean("toggle_visible");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _toggleVisible;
    }

    public CompanyRatingRating toggleVisible(Boolean toggleVisible) throws ParseException {
        _toggleVisible = toggleVisible;
        SOURCE.put("toggle_visible", toggleVisible);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(CompanyRatingRating[] array) {
        JsonArray list = new JsonArray();
        for (CompanyRatingRating item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static CompanyRatingRating[] fromJsonArray(JsonArray array) {
        CompanyRatingRating[] list = new CompanyRatingRating[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static CompanyRatingRating fromJson(JsonObject obj) {
        try {
            return new CompanyRatingRating(obj);
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
    public static final Parcelable.Creator<CompanyRatingRating> CREATOR = new Parcelable.Creator<CompanyRatingRating>() {

        @Override
        public CompanyRatingRating createFromParcel(Parcel source) {
            try {
                return CompanyRatingRating.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public CompanyRatingRating[] newArray(int size) {
            return new CompanyRatingRating[size];
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
