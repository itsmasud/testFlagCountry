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

public class UserJobs implements Parcelable {
    private static final String TAG = "UserJobs";

    @Json(name = "company")
    private Integer _company;

    @Json(name = "marketplace")
    private Integer _marketplace;

    @Source
    private JsonObject SOURCE;

    public UserJobs() {
        SOURCE = new JsonObject();
    }

    public UserJobs(JsonObject obj) {
        SOURCE = obj;
    }

    public void setCompany(Integer company) throws ParseException {
        _company = company;
        SOURCE.put("company", company);
    }

    public Integer getCompany() {
        try {
            if (_company == null && SOURCE.has("company") && SOURCE.get("company") != null)
                _company = SOURCE.getInt("company");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _company;
    }

    public UserJobs company(Integer company) throws ParseException {
        _company = company;
        SOURCE.put("company", company);
        return this;
    }

    public void setMarketplace(Integer marketplace) throws ParseException {
        _marketplace = marketplace;
        SOURCE.put("marketplace", marketplace);
    }

    public Integer getMarketplace() {
        try {
            if (_marketplace == null && SOURCE.has("marketplace") && SOURCE.get("marketplace") != null)
                _marketplace = SOURCE.getInt("marketplace");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _marketplace;
    }

    public UserJobs marketplace(Integer marketplace) throws ParseException {
        _marketplace = marketplace;
        SOURCE.put("marketplace", marketplace);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(UserJobs[] array) {
        JsonArray list = new JsonArray();
        for (UserJobs item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static UserJobs[] fromJsonArray(JsonArray array) {
        UserJobs[] list = new UserJobs[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UserJobs fromJson(JsonObject obj) {
        try {
            return new UserJobs(obj);
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
        dest.writeParcelable(getJson(), flags);
    }

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return true;
    }
}
