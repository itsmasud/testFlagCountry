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

public class CompanyIntegrations implements Parcelable {
    private static final String TAG = "CompanyIntegrations";

    @Json(name = "envelope")
    private ListEnvelope _envelope;

    @Json(name = "results")
    private CompanyIntegration[] _results;

    public CompanyIntegrations() {
    }

    public void setEnvelope(ListEnvelope envelope) {
        _envelope = envelope;
    }

    public ListEnvelope getEnvelope() {
        return _envelope;
    }

    public CompanyIntegrations envelope(ListEnvelope envelope) {
        _envelope = envelope;
        return this;
    }

    public void setResults(CompanyIntegration[] results) {
        _results = results;
    }

    public CompanyIntegration[] getResults() {
        return _results;
    }

    public CompanyIntegrations results(CompanyIntegration[] results) {
        _results = results;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CompanyIntegrations fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CompanyIntegrations.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CompanyIntegrations companyIntegrations) {
        try {
            return Serializer.serializeObject(companyIntegrations);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<CompanyIntegrations> CREATOR = new Parcelable.Creator<CompanyIntegrations>() {

        @Override
        public CompanyIntegrations createFromParcel(Parcel source) {
            try {
                return CompanyIntegrations.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public CompanyIntegrations[] newArray(int size) {
            return new CompanyIntegrations[size];
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
