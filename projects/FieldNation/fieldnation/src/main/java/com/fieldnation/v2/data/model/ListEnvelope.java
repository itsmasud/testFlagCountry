package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger.
 */

public class ListEnvelope implements Parcelable {
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

    public void setPerPage(Integer perPage) {
        _perPage = perPage;
    }

    public Integer getPerPage() {
        return _perPage;
    }

    public ListEnvelope perPage(Integer perPage) {
        _perPage = perPage;
        return this;
    }

    public void setTotal(Integer total) {
        _total = total;
    }

    public Integer getTotal() {
        return _total;
    }

    public ListEnvelope total(Integer total) {
        _total = total;
        return this;
    }

    public void setView(ViewEnum view) {
        _view = view;
    }

    public ViewEnum getView() {
        return _view;
    }

    public ListEnvelope view(ViewEnum view) {
        _view = view;
        return this;
    }

    public void setPages(Integer pages) {
        _pages = pages;
    }

    public Integer getPages() {
        return _pages;
    }

    public ListEnvelope pages(Integer pages) {
        _pages = pages;
        return this;
    }

    public void setColumns(String columns) {
        _columns = columns;
    }

    public String getColumns() {
        return _columns;
    }

    public ListEnvelope columns(String columns) {
        _columns = columns;
        return this;
    }

    public void setAvailableColumns(AvailableColumn[] availableColumns) {
        _availableColumns = availableColumns;
    }

    public AvailableColumn[] getAvailableColumns() {
        return _availableColumns;
    }

    public ListEnvelope availableColumns(AvailableColumn[] availableColumns) {
        _availableColumns = availableColumns;
        return this;
    }

    public void setPage(Integer page) {
        _page = page;
    }

    public Integer getPage() {
        return _page;
    }

    public ListEnvelope page(Integer page) {
        _page = page;
        return this;
    }

    public void setSort(String sort) {
        _sort = sort;
    }

    public String getSort() {
        return _sort;
    }

    public ListEnvelope sort(String sort) {
        _sort = sort;
        return this;
    }

    public void setList(String list) {
        _list = list;
    }

    public String getList() {
        return _list;
    }

    public ListEnvelope list(String list) {
        _list = list;
        return this;
    }

    public void setOrder(OrderEnum order) {
        _order = order;
    }

    public OrderEnum getOrder() {
        return _order;
    }

    public ListEnvelope order(OrderEnum order) {
        _order = order;
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ViewEnum {
        @Json(name = "schedule")
        SCHEDULE("schedule"),
        @Json(name = "list")
        LIST("list"),
        @Json(name = "map")
        MAP("map"),
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

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ListEnvelope[] fromJsonArray(JsonArray array) {
        ListEnvelope[] list = new ListEnvelope[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

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

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<ListEnvelope> CREATOR = new Parcelable.Creator<ListEnvelope>() {

        @Override
        public ListEnvelope createFromParcel(Parcel source) {
            try {
                return ListEnvelope.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ListEnvelope[] newArray(int size) {
            return new ListEnvelope[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(toJson(), flags);
    }
}
