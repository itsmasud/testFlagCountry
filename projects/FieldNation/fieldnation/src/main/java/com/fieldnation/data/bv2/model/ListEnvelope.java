package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class ListEnvelope {
    private static final String TAG = "ListEnvelope";

    @Json(name = "per_page")
    private Integer perPage;

    @Json(name = "total")
    private Integer total;

    @Json(name = "view")
    private ViewEnum view;

    @Json(name = "pages")
    private Integer pages;

    @Json(name = "columns")
    private String columns;

    @Json(name = "available_columns")
    private AvailableColumn[] availableColumns;

    @Json(name = "page")
    private Integer page;

    @Json(name = "sort")
    private String sort;

    @Json(name = "list")
    private String list;

    @Json(name = "order")
    private OrderEnum order;

    public ListEnvelope() {
    }

    public Integer getPerPage() {
        return perPage;
    }

    public Integer getTotal() {
        return total;
    }

    public ViewEnum getView() {
        return view;
    }

    public Integer getPages() {
        return pages;
    }

    public String getColumns() {
        return columns;
    }

    public AvailableColumn[] getAvailableColumns() {
        return availableColumns;
    }

    public Integer getPage() {
        return page;
    }

    public String getSort() {
        return sort;
    }

    public String getList() {
        return list;
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
