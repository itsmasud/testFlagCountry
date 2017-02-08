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

public class WorkOrderPublishStats implements Parcelable {
    private static final String TAG = "WorkOrderPublishStats";

    @Json(name = "routes")
    private Integer _routes;

    @Json(name = "counter_offers")
    private Integer _counterOffers;

    @Json(name = "requests")
    private Integer _requests;

    public WorkOrderPublishStats() {
    }

    public void setRoutes(Integer routes) {
        _routes = routes;
    }

    public Integer getRoutes() {
        return _routes;
    }

    public WorkOrderPublishStats routes(Integer routes) {
        _routes = routes;
        return this;
    }

    public void setCounterOffers(Integer counterOffers) {
        _counterOffers = counterOffers;
    }

    public Integer getCounterOffers() {
        return _counterOffers;
    }

    public WorkOrderPublishStats counterOffers(Integer counterOffers) {
        _counterOffers = counterOffers;
        return this;
    }

    public void setRequests(Integer requests) {
        _requests = requests;
    }

    public Integer getRequests() {
        return _requests;
    }

    public WorkOrderPublishStats requests(Integer requests) {
        _requests = requests;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static WorkOrderPublishStats[] fromJsonArray(JsonArray array) {
        WorkOrderPublishStats[] list = new WorkOrderPublishStats[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderPublishStats fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(WorkOrderPublishStats.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(WorkOrderPublishStats workOrderPublishStats) {
        try {
            return Serializer.serializeObject(workOrderPublishStats);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<WorkOrderPublishStats> CREATOR = new Parcelable.Creator<WorkOrderPublishStats>() {

        @Override
        public WorkOrderPublishStats createFromParcel(Parcel source) {
            try {
                return WorkOrderPublishStats.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderPublishStats[] newArray(int size) {
            return new WorkOrderPublishStats[size];
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
