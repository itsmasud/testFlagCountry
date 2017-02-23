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

public class CompanyRating implements Parcelable {
    private static final String TAG = "CompanyRating";

    @Json(name = "rating")
    private CompanyRatingRating _rating;

    @Source
    private JsonObject SOURCE;

    public CompanyRating() {
        SOURCE = new JsonObject();
    }

    public CompanyRating(JsonObject obj) {
        SOURCE = obj;
    }

    public void setRating(CompanyRatingRating rating) throws ParseException {
        _rating = rating;
        SOURCE.put("rating", rating.getJson());
    }

    public CompanyRatingRating getRating() {
        try {
            if (_rating != null)
                return _rating;

            if (SOURCE.has("rating") && SOURCE.get("rating") != null)
                _rating = CompanyRatingRating.fromJson(SOURCE.getJsonObject("rating"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _rating;
    }

    public CompanyRating rating(CompanyRatingRating rating) throws ParseException {
        _rating = rating;
        SOURCE.put("rating", rating.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(CompanyRating[] array) {
        JsonArray list = new JsonArray();
        for (CompanyRating item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static CompanyRating[] fromJsonArray(JsonArray array) {
        CompanyRating[] list = new CompanyRating[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static CompanyRating fromJson(JsonObject obj) {
        try {
            return new CompanyRating(obj);
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
        dest.writeParcelable(getJson(), flags);
    }
}
