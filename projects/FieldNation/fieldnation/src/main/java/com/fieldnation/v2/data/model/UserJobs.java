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

public class UserJobs implements Parcelable {
    private static final String TAG = "UserJobs";

    @Json(name = "company")
    private Integer _company;

    @Json(name = "marketplace")
    private Integer _marketplace;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public UserJobs() {
    }

    public void setCompany(Integer company) throws ParseException {
        _company = company;
        SOURCE.put("company", company);
    }

    public Integer getCompany() {
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
            return Unserializer.unserializeObject(UserJobs.class, obj);
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
}
