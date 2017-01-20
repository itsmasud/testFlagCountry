package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class AvailableColumnItems {
    private static final String TAG = "AvailableColumnItems";

    @Json(name = "id")
    private String id = null;

    @Json(name = "label")
    private String label = null;

    @Json(name = "group")
    private String group = null;

    @Json(name = "order")
    private Integer order = null;

    @Json(name = "icon")
    private String icon = null;

    @Json(name = "selected")
    private Boolean selected = null;

    @Json(name = "can_sort")
    private Boolean canSort = null;

    @Json(name = "sort_dir")
    private String sortDir = null;

    public AvailableColumnItems() {
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getGroup() {
        return group;
    }

    public Integer getOrder() {
        return order;
    }

    public String getIcon() {
        return icon;
    }

    public Boolean getSelected() {
        return selected;
    }

    public Boolean getCanSort() {
        return canSort;
    }

    public String getSortDir() {
        return sortDir;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static AvailableColumnItems fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(AvailableColumnItems.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}

