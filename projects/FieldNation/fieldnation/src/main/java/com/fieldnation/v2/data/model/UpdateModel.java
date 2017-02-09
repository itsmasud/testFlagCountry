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
 * Created by dmgen from swagger.
 */

public class UpdateModel implements Parcelable {
    private static final String TAG = "UpdateModel";

    @Json(name = "metadata")
    private UpdateModelMetadata _metadata;

    @Json(name = "params")
    private UpdateModelParams _params;

    @Json(name = "service_name")
    private String _serviceName;

    @Json(name = "timestamp")
    private String _timestamp;

    @Json(name = "version")
    private String _version;

    public UpdateModel() {
    }

    public void setMetadata(UpdateModelMetadata metadata) {
        _metadata = metadata;
    }

    public UpdateModelMetadata getMetadata() {
        return _metadata;
    }

    public UpdateModel metadata(UpdateModelMetadata metadata) {
        _metadata = metadata;
        return this;
    }

    public void setParams(UpdateModelParams params) {
        _params = params;
    }

    public UpdateModelParams getParams() {
        return _params;
    }

    public UpdateModel params(UpdateModelParams params) {
        _params = params;
        return this;
    }

    public void setServiceName(String serviceName) {
        _serviceName = serviceName;
    }

    public String getServiceName() {
        return _serviceName;
    }

    public UpdateModel serviceName(String serviceName) {
        _serviceName = serviceName;
        return this;
    }

    public void setTimestamp(String timestamp) {
        _timestamp = timestamp;
    }

    public String getTimestamp() {
        return _timestamp;
    }

    public UpdateModel timestamp(String timestamp) {
        _timestamp = timestamp;
        return this;
    }

    public void setVersion(String version) {
        _version = version;
    }

    public String getVersion() {
        return _version;
    }

    public UpdateModel version(String version) {
        _version = version;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UpdateModel[] fromJsonArray(JsonArray array) {
        UpdateModel[] list = new UpdateModel[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UpdateModel fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UpdateModel.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UpdateModel updateModel) {
        try {
            return Serializer.serializeObject(updateModel);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<UpdateModel> CREATOR = new Parcelable.Creator<UpdateModel>() {

        @Override
        public UpdateModel createFromParcel(Parcel source) {
            try {
                return UpdateModel.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UpdateModel[] newArray(int size) {
            return new UpdateModel[size];
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
