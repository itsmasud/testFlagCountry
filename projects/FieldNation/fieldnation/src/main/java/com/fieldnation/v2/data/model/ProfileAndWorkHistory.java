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

public class ProfileAndWorkHistory implements Parcelable {
    private static final String TAG = "ProfileAndWorkHistory";

    @Json(name = "rating")
    private ProfileAndWorkHistoryRating _rating;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public ProfileAndWorkHistory() {
    }

    public void setRating(ProfileAndWorkHistoryRating rating) throws ParseException {
        _rating = rating;
        SOURCE.put("rating", rating.getJson());
    }

    public ProfileAndWorkHistoryRating getRating() {
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
            return Unserializer.unserializeObject(ProfileAndWorkHistory.class, obj);
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
}
