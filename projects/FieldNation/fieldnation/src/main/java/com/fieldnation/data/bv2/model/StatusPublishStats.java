package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class StatusPublishStats implements Parcelable {
    private static final String TAG = "StatusPublishStats";

    @Json(name = "routes")
    private Integer _routes;

    @Json(name = "counter_offers")
    private Integer _counterOffers;

    @Json(name = "requests")
    private Integer _requests;

    public StatusPublishStats() {
    }

    public void setRoutes(Integer routes) {
        _routes = routes;
    }

    public Integer getRoutes() {
        return _routes;
    }

    public StatusPublishStats routes(Integer routes) {
        _routes = routes;
        return this;
    }

    public void setCounterOffers(Integer counterOffers) {
        _counterOffers = counterOffers;
    }

    public Integer getCounterOffers() {
        return _counterOffers;
    }

    public StatusPublishStats counterOffers(Integer counterOffers) {
        _counterOffers = counterOffers;
        return this;
    }

    public void setRequests(Integer requests) {
        _requests = requests;
    }

    public Integer getRequests() {
        return _requests;
    }

    public StatusPublishStats requests(Integer requests) {
        _requests = requests;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static StatusPublishStats[] fromJsonArray(JsonArray array) {
        StatusPublishStats[] list = new StatusPublishStats[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static StatusPublishStats fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(StatusPublishStats.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(StatusPublishStats statusPublishStats) {
        try {
            return Serializer.serializeObject(statusPublishStats);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<StatusPublishStats> CREATOR = new Parcelable.Creator<StatusPublishStats>() {

        @Override
        public StatusPublishStats createFromParcel(Parcel source) {
            try {
                return StatusPublishStats.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public StatusPublishStats[] newArray(int size) {
            return new StatusPublishStats[size];
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
