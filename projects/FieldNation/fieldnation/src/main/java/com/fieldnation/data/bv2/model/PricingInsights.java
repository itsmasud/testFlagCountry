package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/30/17.
 */

public class PricingInsights implements Parcelable {
    private static final String TAG = "PricingInsights";

    @Json(name = "region")
    private PricingInsightsRegion _region;

    public PricingInsights() {
    }

    public void setRegion(PricingInsightsRegion region) {
        _region = region;
    }

    public PricingInsightsRegion getRegion() {
        return _region;
    }

    public PricingInsights region(PricingInsightsRegion region) {
        _region = region;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PricingInsights fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PricingInsights.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PricingInsights pricingInsights) {
        try {
            return Serializer.serializeObject(pricingInsights);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<PricingInsights> CREATOR = new Parcelable.Creator<PricingInsights>() {

        @Override
        public PricingInsights createFromParcel(Parcel source) {
            try {
                return PricingInsights.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PricingInsights[] newArray(int size) {
            return new PricingInsights[size];
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
