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

public class GetProvidersOptions implements Parcelable {
    private static final String TAG = "GetProvidersOptions";

    @Json(name = "sticky")
    private Boolean _sticky;

    @Json(name = "defaultView")
    private String _defaultView;

    @Json(name = "view")
    private String _view;

    public GetProvidersOptions() {
    }

    public void setSticky(Boolean sticky) {
        _sticky = sticky;
    }

    public Boolean getSticky() {
        return _sticky;
    }

    public GetProvidersOptions sticky(Boolean sticky) {
        _sticky = sticky;
        return this;
    }

    public void setDefaultView(String defaultView) {
        _defaultView = defaultView;
    }

    public String getDefaultView() {
        return _defaultView;
    }

    public GetProvidersOptions defaultView(String defaultView) {
        _defaultView = defaultView;
        return this;
    }

    public void setView(String view) {
        _view = view;
    }

    public String getView() {
        return _view;
    }

    public GetProvidersOptions view(String view) {
        _view = view;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static GetProvidersOptions fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(GetProvidersOptions.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(GetProvidersOptions getProvidersOptions) {
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
    public static final Parcelable.Creator<GetProvidersOptions> CREATOR = new Parcelable.Creator<GetProvidersOptions>() {

        @Override
        public GetProvidersOptions createFromParcel(Parcel source) {
            try {
                return GetProvidersOptions.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public GetProvidersOptions[] newArray(int size) {
            return new GetProvidersOptions[size];
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
