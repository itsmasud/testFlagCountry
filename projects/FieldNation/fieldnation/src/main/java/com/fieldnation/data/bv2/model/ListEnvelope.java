package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class ListEnvelope {
    private static final String TAG = "ListEnvelope";

    @Json(name = "per_page")
    private Integer _perPage;

    @Json(name = "total")
    private Integer _total;

    @Json(name = "view")
    private ViewEnum _view;

    @Json(name = "pages")
    private Integer _pages;

    @Json(name = "columns")
    private String _columns;

    @Json(name = "available_columns")
    private AvailableColumn[] _availableColumns;

    @Json(name = "page")
    private Integer _page;

    @Json(name = "sort")
    private String _sort;

    @Json(name = "list")
    private String _list;

    @Json(name = "order")
    private OrderEnum _order;

    public ListEnvelope() {
    }

    public Integer getPerPage() {
        return _perPage;
    }

    public Integer getTotal() {
        return _total;
    }

    public ViewEnum getView() {
        return _view;
    }

    public Integer getPages() {
        return _pages;
    }

    public String getColumns() {
        return _columns;
    }

    public AvailableColumn[] getAvailableColumns() {
        return _availableColumns;
    }

    public Integer getPage() {
        return _page;
    }

    public String getSort() {
        return _sort;
    }

    public String getList() {
        return _list;
    }

    public OrderEnum getOrder() {
        return _order;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ListEnvelope fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ListEnvelope.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
