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

public class ProfileAndWorkHistoryRating implements Parcelable {
    private static final String TAG = "ProfileAndWorkHistoryRating";

    @Json(name = "more_results")
    private Boolean _moreResults;

    @Json(name = "profile")
    private User _profile;

    @Json(name = "work_history")
    private byte[] _workHistory;

    public ProfileAndWorkHistoryRating() {
    }

    public void setMoreResults(Boolean moreResults) {
        _moreResults = moreResults;
    }

    public Boolean getMoreResults() {
        return _moreResults;
    }

    public ProfileAndWorkHistoryRating moreResults(Boolean moreResults) {
        _moreResults = moreResults;
        return this;
    }

    public void setProfile(User profile) {
        _profile = profile;
    }

    public User getProfile() {
        return _profile;
    }

    public ProfileAndWorkHistoryRating profile(User profile) {
        _profile = profile;
        return this;
    }

    public void setWorkHistory(byte[] workHistory) {
        _workHistory = workHistory;
    }

    public byte[] getWorkHistory() {
        return _workHistory;
    }

    public ProfileAndWorkHistoryRating workHistory(byte[] workHistory) {
        _workHistory = workHistory;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ProfileAndWorkHistoryRating[] fromJsonArray(JsonArray array) {
        ProfileAndWorkHistoryRating[] list = new ProfileAndWorkHistoryRating[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ProfileAndWorkHistoryRating fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ProfileAndWorkHistoryRating.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ProfileAndWorkHistoryRating profileAndWorkHistoryRating) {
        try {
            return Serializer.serializeObject(profileAndWorkHistoryRating);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<ProfileAndWorkHistoryRating> CREATOR = new Parcelable.Creator<ProfileAndWorkHistoryRating>() {

        @Override
        public ProfileAndWorkHistoryRating createFromParcel(Parcel source) {
            try {
                return ProfileAndWorkHistoryRating.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ProfileAndWorkHistoryRating[] newArray(int size) {
            return new ProfileAndWorkHistoryRating[size];
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
