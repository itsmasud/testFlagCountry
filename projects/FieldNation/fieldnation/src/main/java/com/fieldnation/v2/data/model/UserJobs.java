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

public class UserJobs implements Parcelable {
    private static final String TAG = "UserJobs";

    @Json(name = "marketplace")
    private Integer _marketplace;

    @Json(name = "company")
    private Integer _company;

    public UserJobs() {
    }

    public void setMarketplace(Integer marketplace) {
        _marketplace = marketplace;
    }

    public Integer getMarketplace() {
        return _marketplace;
    }

    public UserJobs marketplace(Integer marketplace) {
        _marketplace = marketplace;
        return this;
    }

    public void setCompany(Integer company) {
        _company = company;
    }

    public Integer getCompany() {
        return _company;
    }

    public UserJobs company(Integer company) {
        _company = company;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UserJobs[] fromJsonArray(JsonArray array) {
        UserJobs[] list = new UserJobs[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UserJobs fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UserJobs.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UserJobs userJobs) {
        try {
            return Serializer.serializeObject(userJobs);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<UserJobs> CREATOR = new Parcelable.Creator<UserJobs>() {

        @Override
        public UserJobs createFromParcel(Parcel source) {
            try {
                return UserJobs.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UserJobs[] newArray(int size) {
            return new UserJobs[size];
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
