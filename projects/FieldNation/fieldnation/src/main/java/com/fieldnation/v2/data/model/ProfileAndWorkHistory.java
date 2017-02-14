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

public class ProfileAndWorkHistory implements Parcelable {
    private static final String TAG = "ProfileAndWorkHistory";

    @Json(name = "rating")
    private ProfileAndWorkHistoryRating _rating;

    public ProfileAndWorkHistory() {
    }

    public void setRating(ProfileAndWorkHistoryRating rating) {
        _rating = rating;
    }

    public ProfileAndWorkHistoryRating getRating() {
        return _rating;
    }

    public ProfileAndWorkHistory rating(ProfileAndWorkHistoryRating rating) {
        _rating = rating;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ProfileAndWorkHistory[] fromJsonArray(JsonArray array) {
        ProfileAndWorkHistory[] list = new ProfileAndWorkHistory[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ProfileAndWorkHistory fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ProfileAndWorkHistory.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ProfileAndWorkHistory profileAndWorkHistory) {
        try {
            return Serializer.serializeObject(profileAndWorkHistory);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
