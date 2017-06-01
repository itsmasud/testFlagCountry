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

public class PublishOptions implements Parcelable {
    private static final String TAG = "PublishOptions";

    @Json(name = "async")
    private Boolean _async;

    @Json(name = "body")
    private Publish _body;

    public PublishOptions() {
    }

    public void setAsync(Boolean async) {
        _async = async;
    }

    public Boolean getAsync() {
        return _async;
    }

    public PublishOptions async(Boolean async) {
        _async = async;
        return this;
    }

    public boolean isAsyncSet() {
        return _async != null;
    }

    public String getAsyncUrlParam() {
        return "async=" + _async;
    }

    public void setBody(Publish body) {
        _body = body;
    }

    public Publish getBody() {
        return _body;
    }

    public PublishOptions body(Publish body) {
        _body = body;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PublishOptions fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PublishOptions.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PublishOptions publishOptions) {
        try {
            return Serializer.serializeObject(publishOptions);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<PublishOptions> CREATOR = new Parcelable.Creator<PublishOptions>() {

        @Override
        public PublishOptions createFromParcel(Parcel source) {
            try {
                return PublishOptions.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PublishOptions[] newArray(int size) {
            return new PublishOptions[size];
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
