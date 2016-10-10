package com.fieldnation.data.v2.actions;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by Michael on 10/10/2016.
 */

public class ActionContainer {
    private static final String TAG = "ActionContainer";

    @Json(name = "primary")
    private Action[] _primary;
    @Json(name = "secondary")
    private Action[] _secondary;

    public ActionContainer() {
    }

    public Action[] getPrimary() {
        return _primary;
    }

    public Action[] getSecondary() {
        return _secondary;
    }

    /*-*************************************-*/
    /*-			JSON Implementation			-*/
    /*-*************************************-*/
    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ActionContainer schedule) {
        try {
            return Serializer.serializeObject(schedule);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static ActionContainer fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(ActionContainer.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
