package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class UpdateModel {
    private static final String TAG = "UpdateModel";

    @Json(name = "metadata")
    private UpdateModelMetadata _metadata;

    @Json(name = "service_name")
    private String _serviceName;

    @Json(name = "params")
    private UpdateModelParams _params;

    @Json(name = "version")
    private String _version;

    @Json(name = "timestamp")
    private String _timestamp;

    public UpdateModel() {
    }

    public UpdateModelMetadata getMetadata() {
        return _metadata;
    }

    public String getServiceName() {
        return _serviceName;
    }

    public UpdateModelParams getParams() {
        return _params;
    }

    public String getVersion() {
        return _version;
    }

    public String getTimestamp() {
        return _timestamp;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
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
}
