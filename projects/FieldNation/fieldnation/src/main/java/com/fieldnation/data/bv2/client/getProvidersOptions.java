package com.fieldnation.data.bv2.client;

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

public class getProvidersOptions implements Parcelable {
    private static final String TAG = "getProvidersOptions";

    @Json(name = "sticky")
    private Boolean _sticky;

    @Json(name = "defaultView")
    private String _defaultView;

    @Json(name = "view")
    private String _view;

    public getProvidersOptions() {
    }

    public void setSticky(Boolean sticky) {
        _sticky = sticky;
    }

    public Boolean getSticky() {
        return _sticky;
    }

    public getProvidersOptions sticky(Boolean sticky) {
        _sticky = sticky;
        return this;
    }

    public void setDefaultView(String defaultView) {
        _defaultView = defaultView;
    }

    public String getDefaultView() {
        return _defaultView;
    }

    public getProvidersOptions defaultView(String defaultView) {
        _defaultView = defaultView;
        return this;
    }

    public void setView(String view) {
        _view = view;
    }

    public String getView() {
        return _view;
    }

    public getProvidersOptions view(String view) {
        _view = view;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static getProvidersOptions fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(getProvidersOptions.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(getProvidersOptions getProvidersOptions) {
        try {
            return Serializer.serializeObject(getProvidersOptions);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<getProvidersOptions> CREATOR = new Parcelable.Creator<getProvidersOptions>() {

        @Override
        public getProvidersOptions createFromParcel(Parcel source) {
            try {
                return getProvidersOptions.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public getProvidersOptions[] newArray(int size) {
            return new getProvidersOptions[size];
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
