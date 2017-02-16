package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class SwapResponse implements Parcelable {
    private static final String TAG = "SwapResponse";

    @Json(name = "success")
    private Boolean _success;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public SwapResponse() {
    }

    public void setSuccess(Boolean success) throws ParseException {
        _success = success;
        SOURCE.put("success", success);
    }

    public Boolean getSuccess() {
        return _success;
    }

    public SwapResponse success(Boolean success) throws ParseException {
        _success = success;
        SOURCE.put("success", success);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(SwapResponse[] array) {
        JsonArray list = new JsonArray();
        for (SwapResponse item : array) {
            list.add(item.getJson());
        }
        return list;
    }

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

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}
