package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class ListEnvelope {
    private static final String TAG = "ListEnvelope";

    @Json(name = "total")
    private Integer total = null;

    @Json(name = "page")
    private Integer page = null;

    @Json(name = "pages")
    private Integer pages = null;

    @Json(name = "per_page")
    private Integer perPage = null;

    @Json(name = "columns")
    private String columns = null;

    @Json(name = "available_columns")
    private AvailableColumn[] availableColumns;

    @Json(name = "list")
    private String list = null;

    @Json(name = "view")
    private ViewEnum view = null;

    @Json(name = "sort")
    private String sort = null;

    @Json(name = "order")
    private OrderEnum order = null;


    public enum ViewEnum {
        @Json(name = "map")
        MAP("map"),
        @Json(name = "list")
        LIST("list"),
        @Json(name = "schedule")
        SCHEDULE("schedule"),
        @Json(name = "card")
        CARD("card");

        private String value;

        ViewEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

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

    public ListEnvelope() {
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPages() {
        return pages;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public String getColumns() {
        return columns;
    }

    public AvailableColumn[] getAvailableColumns() {
        return availableColumns;
    }

    public String getList() {
        return list;
    }

    public ViewEnum getView() {
        return view;
    }

    public String getSort() {
        return sort;
    }

    public OrderEnum getOrder() {
        return order;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ListEnvelope fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ListEnvelope.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ListEnvelope listEnvelope) {
        try {
            return Serializer.serializeObject(listEnvelope);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}

