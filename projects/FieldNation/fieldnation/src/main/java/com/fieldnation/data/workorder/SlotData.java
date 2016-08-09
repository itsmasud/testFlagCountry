package com.fieldnation.data.workorder;

import com.fieldnation.fnlog.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.Unserializer;
import com.fieldnation.json.annotations.Json;

public class SlotData {
    private static final String TAG = "SlotData";

    @Json(name = "maxFileSize")
    private Long _maxFileSize;
    @Json(name = "maxFiles")
    private Integer _maxFiles;
    @Json(name = "minFileSize")
    private Long _minFileSize;
    @Json(name = "minFiles")
    private Integer _minFiles;
    @Json(name = "slotId")
    private Integer _slotId;
    @Json(name = "slotName")
    private String _slotName;

    public SlotData() {
    }

    public Long getMaxFileSize() {
        return _maxFileSize;
    }

    public Integer getMaxFiles() {
        return _maxFiles;
    }

    public Long getMinFileSize() {
        return _minFileSize;
    }

    public Integer getMinFiles() {
        return _minFiles;
    }

    public Integer getSlotId() {
        return _slotId;
    }

    public String getSlotName() {
        return _slotName;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(SlotData slotData) {
        try {
            return Serializer.serializeObject(slotData);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static SlotData fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(SlotData.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

}
