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

public class CompanyRating implements Parcelable {
    private static final String TAG = "CompanyRating";

    @Json(name = "rating")
    private CompanyRatingRating _rating;

    public CompanyRating() {
    }

    public void setRating(CompanyRatingRating rating) {
        _rating = rating;
    }

    public CompanyRatingRating getRating() {
        return _rating;
    }

    public CompanyRating rating(CompanyRatingRating rating) {
        _rating = rating;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CompanyRating[] fromJsonArray(JsonArray array) {
        CompanyRating[] list = new CompanyRating[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static CompanyRating fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CompanyRating.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CompanyRating companyRating) {
        try {
            return Serializer.serializeObject(companyRating);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<CompanyRating> CREATOR = new Parcelable.Creator<CompanyRating>() {

        @Override
        public CompanyRating createFromParcel(Parcel source) {
            try {
                return CompanyRating.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public CompanyRating[] newArray(int size) {
            return new CompanyRating[size];
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
