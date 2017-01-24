package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class UpdateModel {
    private static final String TAG = "UpdateModel";

    @Json(name = "metadata")
    private UpdateModelMetadata metadata;

    @Json(name = "service_name")
    private String serviceName;

    @Json(name = "params")
    private UpdateModelParams params;

    @Json(name = "version")
    private String version;

    @Json(name = "timestamp")
    private String timestamp;

    public UpdateModel() {
    }

    public UpdateModelMetadata getMetadata() {
        return metadata;
    }

    public String getServiceName() {
        return serviceName;
    }

    public UpdateModelParams getParams() {
        return params;
    }

    public String getVersion() {
        return version;
    }

    public String getTimestamp() {
        return timestamp;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UpdateModel fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UpdateModel.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}
