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

public class CompanyRatingRating implements Parcelable {
    private static final String TAG = "CompanyRatingRating";

    @Json(name = "marketplace")
    private Satisfaction _marketplace;

    @Json(name = "mine")
    private Satisfaction _mine;

    @Json(name = "toggle_visible")
    private Boolean _toggleVisible;

    public CompanyRatingRating() {
    }

    public void setMarketplace(Satisfaction marketplace) {
        _marketplace = marketplace;
    }

    public Satisfaction getMarketplace() {
        return _marketplace;
    }

    public CompanyRatingRating marketplace(Satisfaction marketplace) {
        _marketplace = marketplace;
        return this;
    }

    public void setMine(Satisfaction mine) {
        _mine = mine;
    }

    public Satisfaction getMine() {
        return _mine;
    }

    public CompanyRatingRating mine(Satisfaction mine) {
        _mine = mine;
        return this;
    }

    public void setToggleVisible(Boolean toggleVisible) {
        _toggleVisible = toggleVisible;
    }

    public Boolean getToggleVisible() {
        return _toggleVisible;
    }

    public CompanyRatingRating toggleVisible(Boolean toggleVisible) {
        _toggleVisible = toggleVisible;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CompanyRatingRating[] fromJsonArray(JsonArray array) {
        CompanyRatingRating[] list = new CompanyRatingRating[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static CompanyRatingRating fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CompanyRatingRating.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CompanyRatingRating companyRatingRating) {
        try {
            return Serializer.serializeObject(companyRatingRating);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
