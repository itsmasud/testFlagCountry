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

public class ProfileAndWorkHistory implements Parcelable {
    private static final String TAG = "ProfileAndWorkHistory";

    @Json(name = "rating")
    private ProfileAndWorkHistoryRating _rating;

    @Source
    private JsonObject SOURCE;

    public ProfileAndWorkHistory() {
        SOURCE = new JsonObject();
    }

    public ProfileAndWorkHistory(JsonObject obj) {
        SOURCE = obj;
    }

    public void setRating(ProfileAndWorkHistoryRating rating) throws ParseException {
        _rating = rating;
        SOURCE.put("rating", rating.getJson());
    }

    public ProfileAndWorkHistoryRating getRating() {
        try {
            if (_rating == null && SOURCE.has("rating") && SOURCE.get("rating") != null)
                _rating = ProfileAndWorkHistoryRating.fromJson(SOURCE.getJsonObject("rating"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_rating == null)
            _rating = new ProfileAndWorkHistoryRating();

        return _rating;
    }

    public ProfileAndWorkHistory rating(ProfileAndWorkHistoryRating rating) throws ParseException {
        _rating = rating;
        SOURCE.put("rating", rating.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(ProfileAndWorkHistory[] array) {
        JsonArray list = new JsonArray();
        for (ProfileAndWorkHistory item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static ProfileAndWorkHistory[] fromJsonArray(JsonArray array) {
        ProfileAndWorkHistory[] list = new ProfileAndWorkHistory[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ProfileAndWorkHistory fromJson(JsonObject obj) {
        try {
            return new ProfileAndWorkHistory(obj);
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
    public static final Parcelable.Creator<ProfileAndWorkHistory> CREATOR = new Parcelable.Creator<ProfileAndWorkHistory>() {

        @Override
        public ProfileAndWorkHistory createFromParcel(Parcel source) {
            try {
                return ProfileAndWorkHistory.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ProfileAndWorkHistory[] newArray(int size) {
            return new ProfileAndWorkHistory[size];
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
