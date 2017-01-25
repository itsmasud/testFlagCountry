package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class SavedList {
    private static final String TAG = "SavedList";

    @Json(name = "default")
    private Boolean _default;

    @Json(name = "columns")
    private String _columns;

    @Json(name = "count")
    private Integer _count;

    @Json(name = "id")
    private String _id;

    @Json(name = "sort")
    private String _sort;

    @Json(name = "label")
    private String _label;

    @Json(name = "order")
    private OrderEnum _order;

    public SavedList() {
    }

    public Boolean getDefault() {
        return _default;
    }

    public String getColumns() {
        return _columns;
    }

    public Integer getCount() {
        return _count;
    }

    public String getId() {
        return _id;
    }

    public String getSort() {
        return _sort;
    }

    public String getLabel() {
        return _label;
    }

    public OrderEnum getOrder() {
        return _order;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static SavedList fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(SavedList.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(SavedList savedList) {
        try {
            return Serializer.serializeObject(savedList);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
