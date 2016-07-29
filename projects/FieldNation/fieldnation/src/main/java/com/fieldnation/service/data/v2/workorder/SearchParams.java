package com.fieldnation.service.data.v2.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.Unserializer;
import com.fieldnation.json.annotations.Json;

/**
 * Created by Michael on 7/21/2016.
 */
public class SearchParams implements Parcelable {
    private static final String TAG = "SearchParams";

    @Json
    public String status = "available"; // available, assigned, requested, completed
    @Json
    public Double latitude = null;
    @Json
    public Double longitude = null;
    @Json
    public Double radius = null;
    @Json
    public String sort = "time";
    @Json
    public String order = "asc";

    public SearchParams() {
    }

    public SearchParams status(String status) {
        this.status = status;
        return this;
    }

    public SearchParams location(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        return this;
    }

    public SearchParams radius(Double radius) {
        this.radius = radius;
        return this;
    }

    public String toUrlParams() {
        String params = "?status=" + status;

        if (latitude != null && longitude != null)
            params += "&lat=" + latitude + "&lng=" + longitude;

        if (radius != null)
            params += "&radius=" + radius;

        if (sort != null && order != null)
            params += "&sort=" + sort + "&order=" + order;

        return params;
    }

    public String toKey() {
        String key = status;

        if (latitude != null && longitude != null && radius != null) {
            key += ":" + ((int) (latitude * 1000)) + ":" + ((int) (longitude * 1000));
        }

        if (radius != null) {
            key += ":" + ((int) (radius * 1000));
        }
        return key;
    }

    /*-*************************************-*/
    /*-			JSON Implementation			-*/
    /*-*************************************-*/
    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(SearchParams searchParams) {
        try {
            return Serializer.serializeObject(searchParams);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static SearchParams fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(SearchParams.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<SearchParams> CREATOR = new Parcelable.Creator<SearchParams>() {

        @Override
        public SearchParams createFromParcel(Parcel source) {
            try {
                return SearchParams.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public SearchParams[] newArray(int size) {
            return new SearchParams[size];
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
