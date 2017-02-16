package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

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

    @Source
    private JsonObject SOURCE = new JsonObject();

    public ProfileAndWorkHistoryRating() {
    }

    public void setMoreResults(Boolean moreResults) throws ParseException {
        _moreResults = moreResults;
        SOURCE.put("more_results", moreResults);
    }

    public Boolean getMoreResults() {
        return _moreResults;
    }

    public ProfileAndWorkHistoryRating moreResults(Boolean moreResults) throws ParseException {
        _moreResults = moreResults;
        SOURCE.put("more_results", moreResults);
        return this;
    }

    public void setProfile(User profile) throws ParseException {
        _profile = profile;
        SOURCE.put("profile", profile.getJson());
    }

    public User getProfile() {
        return _profile;
    }

    public ProfileAndWorkHistoryRating profile(User profile) throws ParseException {
        _profile = profile;
        SOURCE.put("profile", profile.getJson());
        return this;
    }

    public void setWorkHistory(byte[] workHistory) throws ParseException {
        _workHistory = workHistory;
        SOURCE.put("work_history", workHistory);
    }

    public byte[] getWorkHistory() {
        return _workHistory;
    }

    public ProfileAndWorkHistoryRating workHistory(byte[] workHistory) throws ParseException {
        _workHistory = workHistory;
        SOURCE.put("work_history", workHistory);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(ProfileAndWorkHistoryRating[] array) {
        JsonArray list = new JsonArray();
        for (ProfileAndWorkHistoryRating item : array) {
            list.add(item.getJson());
        }
        return list;
    }

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

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}
