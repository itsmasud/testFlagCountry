package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
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
    private JsonObject _workHistory;

    @Source
    private JsonObject SOURCE;

    public ProfileAndWorkHistoryRating() {
        SOURCE = new JsonObject();
    }

    public ProfileAndWorkHistoryRating(JsonObject obj) {
        SOURCE = obj;
    }

    public void setMoreResults(Boolean moreResults) throws ParseException {
        _moreResults = moreResults;
        SOURCE.put("more_results", moreResults);
    }

    public Boolean getMoreResults() {
        try {
            if (_moreResults == null && SOURCE.has("more_results") && SOURCE.get("more_results") != null)
                _moreResults = SOURCE.getBoolean("more_results");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_profile == null && SOURCE.has("profile") && SOURCE.get("profile") != null)
                _profile = User.fromJson(SOURCE.getJsonObject("profile"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_profile != null && _profile.isSet())
            return _profile;

        return null;
    }

    public ProfileAndWorkHistoryRating profile(User profile) throws ParseException {
        _profile = profile;
        SOURCE.put("profile", profile.getJson());
        return this;
    }

    public void setWorkHistory(JsonObject workHistory) throws ParseException {
        _workHistory = workHistory;
        SOURCE.put("work_history", workHistory);
    }

    public JsonObject getWorkHistory() {
        try {
            if (_workHistory == null && SOURCE.has("work_history") && SOURCE.get("work_history") != null)
                _workHistory = SOURCE.getJsonObject("work_history");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _workHistory;
    }

    public ProfileAndWorkHistoryRating workHistory(JsonObject workHistory) throws ParseException {
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
            return new ProfileAndWorkHistoryRating(obj);
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

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return true;
    }
}
