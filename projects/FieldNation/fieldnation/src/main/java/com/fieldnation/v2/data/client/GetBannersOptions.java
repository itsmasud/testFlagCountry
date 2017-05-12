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

public class GetBannersOptions implements Parcelable {
    private static final String TAG = "GetBannersOptions";

    @Json(name = "active")
    private Boolean _active;

    @Json(name = "allowedBanners")
    private Boolean _allowedBanners;

    public GetBannersOptions() {
    }

    public void setActive(Boolean active) {
        _active = active;
    }

    public Boolean getActive() {
        return _active;
    }

    public GetBannersOptions active(Boolean active) {
        _active = active;
        return this;
    }

    public boolean isActiveSet() {
        return _active != null;
    }

    public String getActiveUrlParam() {
        return "active=" + _active;
    }

    public void setAllowedBanners(Boolean allowedBanners) {
        _allowedBanners = allowedBanners;
    }

    public Boolean getAllowedBanners() {
        return _allowedBanners;
    }

    public GetBannersOptions allowedBanners(Boolean allowedBanners) {
        _allowedBanners = allowedBanners;
        return this;
    }

    public boolean isAllowedBannersSet() {
        return _allowedBanners != null;
    }

    public String getAllowedBannersUrlParam() {
        return "allowedBanners=" + _allowedBanners;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static GetBannersOptions fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(GetBannersOptions.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(GetBannersOptions getBannersOptions) {
        try {
            return Serializer.serializeObject(getBannersOptions);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<GetBannersOptions> CREATOR = new Parcelable.Creator<GetBannersOptions>() {

        @Override
        public GetBannersOptions createFromParcel(Parcel source) {
            try {
                return GetBannersOptions.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public GetBannersOptions[] newArray(int size) {
            return new GetBannersOptions[size];
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
