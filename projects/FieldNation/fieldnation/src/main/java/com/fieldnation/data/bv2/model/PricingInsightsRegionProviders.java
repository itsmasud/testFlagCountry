package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class PricingInsightsRegionProviders implements Parcelable {
    private static final String TAG = "PricingInsightsRegionProviders";

    @Json(name = "marketplace")
    private Integer _marketplace;

    public PricingInsightsRegionProviders() {
    }

    public void setMarketplace(Integer marketplace) {
        _marketplace = marketplace;
    }

    public Integer getMarketplace() {
        return _marketplace;
    }

    public PricingInsightsRegionProviders marketplace(Integer marketplace) {
        _marketplace = marketplace;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PricingInsightsRegionProviders fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PricingInsightsRegionProviders.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PricingInsightsRegionProviders pricingInsightsRegionProviders) {
        try {
            return Serializer.serializeObject(pricingInsightsRegionProviders);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<PricingInsightsRegionProviders> CREATOR = new Parcelable.Creator<PricingInsightsRegionProviders>() {

        @Override
        public PricingInsightsRegionProviders createFromParcel(Parcel source) {
            try {
                return PricingInsightsRegionProviders.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PricingInsightsRegionProviders[] newArray(int size) {
            return new PricingInsightsRegionProviders[size];
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
