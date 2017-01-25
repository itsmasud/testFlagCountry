package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class UpdateModelParamsModel {
    private static final String TAG = "UpdateModelParamsModel";

    public UpdateModelParamsModel() {
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UpdateModelParamsModel fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UpdateModelParamsModel.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UpdateModelParamsModel updateModelParamsModel) {
        try {
            return Serializer.serializeObject(updateModelParamsModel);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
