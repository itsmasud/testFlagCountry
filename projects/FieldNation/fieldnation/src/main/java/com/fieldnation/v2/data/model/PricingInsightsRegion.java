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

public class PricingInsightsRegion implements Parcelable {
    private static final String TAG = "PricingInsightsRegion";

    @Json(name = "distance")
    private Double _distance;

    @Json(name = "name")
    private String _name;

    @Json(name = "average_rate")
    private PricingInsightsRegionAverageRate _averageRate;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "providers")
    private PricingInsightsRegionProviders _providers;

    public PricingInsightsRegion() {
    }

    public void setDistance(Double distance) {
        _distance = distance;
    }

    public Double getDistance() {
        return _distance;
    }

    public PricingInsightsRegion distance(Double distance) {
        _distance = distance;
        return this;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public PricingInsightsRegion name(String name) {
        _name = name;
        return this;
    }

    public void setAverageRate(PricingInsightsRegionAverageRate averageRate) {
        _averageRate = averageRate;
    }

    public PricingInsightsRegionAverageRate getAverageRate() {
        return _averageRate;
    }

    public PricingInsightsRegion averageRate(PricingInsightsRegionAverageRate averageRate) {
        _averageRate = averageRate;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public PricingInsightsRegion id(Integer id) {
        _id = id;
        return this;
    }

    public void setProviders(PricingInsightsRegionProviders providers) {
        _providers = providers;
    }

    public PricingInsightsRegionProviders getProviders() {
        return _providers;
    }

    public PricingInsightsRegion providers(PricingInsightsRegionProviders providers) {
        _providers = providers;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PricingInsightsRegion[] fromJsonArray(JsonArray array) {
        PricingInsightsRegion[] list = new PricingInsightsRegion[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static PricingInsightsRegion fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PricingInsightsRegion.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PricingInsightsRegion pricingInsightsRegion) {
        try {
            return Serializer.serializeObject(pricingInsightsRegion);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<PricingInsightsRegion> CREATOR = new Parcelable.Creator<PricingInsightsRegion>() {

        @Override
        public PricingInsightsRegion createFromParcel(Parcel source) {
            try {
                return PricingInsightsRegion.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PricingInsightsRegion[] newArray(int size) {
            return new PricingInsightsRegion[size];
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
