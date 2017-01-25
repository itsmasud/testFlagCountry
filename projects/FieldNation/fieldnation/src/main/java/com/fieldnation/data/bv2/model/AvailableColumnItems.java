package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class AvailableColumnItems {
    private static final String TAG = "AvailableColumnItems";

    @Json(name = "icon")
    private String _icon;

    @Json(name = "id")
    private String _id;

    @Json(name = "label")
    private String _label;

    @Json(name = "can_sort")
    private Boolean _canSort;

    @Json(name = "selected")
    private Boolean _selected;

    @Json(name = "sort_dir")
    private String _sortDir;

    @Json(name = "group")
    private String _group;

    @Json(name = "order")
    private Integer _order;

    public AvailableColumnItems() {
    }

    public String getIcon() {
        return _icon;
    }

    public String getId() {
        return _id;
    }

    public String getLabel() {
        return _label;
    }

    public Boolean getCanSort() {
        return _canSort;
    }

    public Boolean getSelected() {
        return _selected;
    }

    public String getSortDir() {
        return _sortDir;
    }

    public String getGroup() {
        return _group;
    }

    public Integer getOrder() {
        return _order;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static AvailableColumnItems fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(AvailableColumnItems.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(AvailableColumnItems availableColumnItems) {
        try {
            return Serializer.serializeObject(availableColumnItems);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
