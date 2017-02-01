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
 * Created by dmgen from swagger on 1/31/17.
 */

public class SwapResponse implements Parcelable {
    private static final String TAG = "SwapResponse";

    @Json(name = "success")
    private Boolean _success;

    public SwapResponse() {
    }

    public void setSuccess(Boolean success) {
        _success = success;
    }

    public Boolean getSuccess() {
        return _success;
    }

    public SwapResponse success(Boolean success) {
        _success = success;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static SwapResponse[] fromJsonArray(JsonArray array) {
        SwapResponse[] list = new SwapResponse[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static SwapResponse fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(SwapResponse.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(SwapResponse swapResponse) {
        try {
            return Serializer.serializeObject(swapResponse);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<SwapResponse> CREATOR = new Parcelable.Creator<SwapResponse>() {

        @Override
        public SwapResponse createFromParcel(Parcel source) {
            try {
                return SwapResponse.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public SwapResponse[] newArray(int size) {
            return new SwapResponse[size];
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
