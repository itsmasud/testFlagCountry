package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class SavedList {
    private static final String TAG = "SavedList";

    @Json(name = "id")
    private String id = null;

    @Json(name = "count")
    private Integer count = null;

    @Json(name = "default")
    private Boolean _default = null;

    @Json(name = "sort")
    private String sort = null;

    @Json(name = "order")
    private OrderEnum order = null;

    @Json(name = "columns")
    private String columns = null;

    @Json(name = "label")
    private String label = null;

    public enum OrderEnum {
        @Json(name = "asc")
        ASC("asc"),
        @Json(name = "desc")
        DESC("desc");

        private String value;

        OrderEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public SavedList() {
    }

    public String getId() {
        return id;
    }

    public Integer getCount() {
        return count;
    }

    public Boolean getDefault() {
        return _default;
    }

    public String getSort() {
        return sort;
    }

    public OrderEnum getOrder() {
        return order;
    }

    public String getColumns() {
        return columns;
    }

    public String getLabel() {
        return label;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static SavedList fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(SavedList.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}