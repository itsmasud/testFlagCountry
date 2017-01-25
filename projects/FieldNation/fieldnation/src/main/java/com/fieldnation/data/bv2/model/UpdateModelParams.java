package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class UpdateModelParams {
    private static final String TAG = "UpdateModelParams";

    @Json(name = "model")
    private UpdateModelParamsModel _model;

    @Json(name = "updateScheduleByWorkOrder")
    private EventUpdateScheduleByWorkOrder _updateScheduleByWorkOrder;

    public UpdateModelParams() {
    }

    public UpdateModelParamsModel getModel() {
        return _model;
    }

    public EventUpdateScheduleByWorkOrder getUpdateScheduleByWorkOrder() {
        return _updateScheduleByWorkOrder;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UpdateModelParams fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UpdateModelParams.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UpdateModelParams updateModelParams) {
        try {
            return Serializer.serializeObject(updateModelParams);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
