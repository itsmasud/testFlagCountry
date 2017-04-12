package com.fieldnation.v2.data.client;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.v2.data.model.*;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger.
 */

public class GetManagedProvidersOptions implements Parcelable {
    private static final String TAG = "GetManagedProvidersOptions";

    @Json(name = "companyId")
    private Integer _companyId;

    @Json(name = "marketplaceOn")
    private Integer _marketplaceOn;

    public GetManagedProvidersOptions() {
    }

    public void setCompanyId(Integer companyId) {
        _companyId = companyId;
    }

    public Integer getCompanyId() {
        return _companyId;
    }

    public GetManagedProvidersOptions companyId(Integer companyId) {
        _companyId = companyId;
        return this;
    }

    public boolean isCompanyIdSet() {
        return _companyId != null;
    }

    public String getCompanyIdUrlParam() {
        return "company_id=" + _companyId;
    }

    public void setMarketplaceOn(Integer marketplaceOn) {
        _marketplaceOn = marketplaceOn;
    }

    public Integer getMarketplaceOn() {
        return _marketplaceOn;
    }

    public GetManagedProvidersOptions marketplaceOn(Integer marketplaceOn) {
        _marketplaceOn = marketplaceOn;
        return this;
    }

    public boolean isMarketplaceOnSet() {
        return _marketplaceOn != null;
    }

    public String getMarketplaceOnUrlParam() {
        return "marketplace_on=" + _marketplaceOn;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static GetManagedProvidersOptions fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(GetManagedProvidersOptions.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(GetManagedProvidersOptions getManagedProvidersOptions) {
        try {
            return Serializer.serializeObject(getManagedProvidersOptions);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<GetManagedProvidersOptions> CREATOR = new Parcelable.Creator<GetManagedProvidersOptions>() {

        @Override
        public GetManagedProvidersOptions createFromParcel(Parcel source) {
            try {
                return GetManagedProvidersOptions.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public GetManagedProvidersOptions[] newArray(int size) {
            return new GetManagedProvidersOptions[size];
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
