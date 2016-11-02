package com.fieldnation.data.workorder;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Label {
    private static final String TAG = "Label";

    @Json(name = "action")
    private String _action;
    @Json(name = "description")
    private String _description;
    @Json(name = "labelId")
    private Integer _labelId;
    @Json(name = "name")
    private String _name;
    @Json(name = "type")
    private String _type;

    public Label() {
    }

    public String getAction() {
        return _action;
    }

    public String getDescription() {
        return _description;
    }

    public Integer getLabelId() {
        return _labelId;
    }

    public String getName() {
        return _name;
    }

    public String getType() {
        return _type;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Label label) {
        try {
            return Serializer.serializeObject(label);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Label fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Label.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

}
