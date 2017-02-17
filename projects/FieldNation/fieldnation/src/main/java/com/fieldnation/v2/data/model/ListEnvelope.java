package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class ListEnvelope implements Parcelable {
    private static final String TAG = "ListEnvelope";

    @Json(name = "available_columns")
    private AvailableColumn[] _availableColumns;

    @Json(name = "columns")
    private String _columns;

    @Json(name = "list")
    private String _list;

    @Json(name = "order")
    private OrderEnum _order;

    @Json(name = "page")
    private Integer _page;

    @Json(name = "pages")
    private Integer _pages;

    @Json(name = "per_page")
    private Integer _perPage;

    @Json(name = "sort")
    private String _sort;

    @Json(name = "total")
    private Integer _total;

    @Json(name = "view")
    private ViewEnum _view;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public ListEnvelope() {
    }

    public void setAvailableColumns(AvailableColumn[] availableColumns) throws ParseException {
        _availableColumns = availableColumns;
        SOURCE.put("available_columns", AvailableColumn.toJsonArray(availableColumns));
    }

    public AvailableColumn[] getAvailableColumns() {
        return _availableColumns;
    }

    public ListEnvelope availableColumns(AvailableColumn[] availableColumns) throws ParseException {
        _availableColumns = availableColumns;
        SOURCE.put("available_columns", AvailableColumn.toJsonArray(availableColumns), true);
        return this;
    }

    public void setColumns(String columns) throws ParseException {
        _columns = columns;
        SOURCE.put("columns", columns);
    }

    public String getColumns() {
        return _columns;
    }

    public ListEnvelope columns(String columns) throws ParseException {
        _columns = columns;
        SOURCE.put("columns", columns);
        return this;
    }

    public void setList(String list) throws ParseException {
        _list = list;
        SOURCE.put("list", list);
    }

    public String getList() {
        return _list;
    }

    public ListEnvelope list(String list) throws ParseException {
        _list = list;
        SOURCE.put("list", list);
        return this;
    }

    public void setOrder(OrderEnum order) throws ParseException {
        _order = order;
        SOURCE.put("order", order.toString());
    }

    public OrderEnum getOrder() {
        return _order;
    }

    public ListEnvelope order(OrderEnum order) throws ParseException {
        _order = order;
        SOURCE.put("order", order.toString());
        return this;
    }

    public void setPage(Integer page) throws ParseException {
        _page = page;
        SOURCE.put("page", page);
    }

    public Integer getPage() {
        return _page;
    }

    public ListEnvelope page(Integer page) throws ParseException {
        _page = page;
        SOURCE.put("page", page);
        return this;
    }

    public void setPages(Integer pages) throws ParseException {
        _pages = pages;
        SOURCE.put("pages", pages);
    }

    public Integer getPages() {
        return _pages;
    }

    public ListEnvelope pages(Integer pages) throws ParseException {
        _pages = pages;
        SOURCE.put("pages", pages);
        return this;
    }

    public void setPerPage(Integer perPage) throws ParseException {
        _perPage = perPage;
        SOURCE.put("per_page", perPage);
    }

    public Integer getPerPage() {
        return _perPage;
    }

    public ListEnvelope perPage(Integer perPage) throws ParseException {
        _perPage = perPage;
        SOURCE.put("per_page", perPage);
        return this;
    }

    public void setSort(String sort) throws ParseException {
        _sort = sort;
        SOURCE.put("sort", sort);
    }

    public String getSort() {
        return _sort;
    }

    public ListEnvelope sort(String sort) throws ParseException {
        _sort = sort;
        SOURCE.put("sort", sort);
        return this;
    }

    public void setTotal(Integer total) throws ParseException {
        _total = total;
        SOURCE.put("total", total);
    }

    public Integer getTotal() {
        return _total;
    }

    public ListEnvelope total(Integer total) throws ParseException {
        _total = total;
        SOURCE.put("total", total);
        return this;
    }

    public void setView(ViewEnum view) throws ParseException {
        _view = view;
        SOURCE.put("view", view.toString());
    }

    public ViewEnum getView() {
        return _view;
    }

    public ListEnvelope view(ViewEnum view) throws ParseException {
        _view = view;
        SOURCE.put("view", view.toString());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ViewEnum {
        @Json(name = "card")
        CARD("card"),
        @Json(name = "list")
        LIST("list"),
        @Json(name = "map")
        MAP("map"),
        @Json(name = "model")
        MODEL("model"),
        @Json(name = "schedule")
        SCHEDULE("schedule");

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
        DESC("desc"),
        @Json(name = "false")
        FALSE("false");

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
    public static JsonArray toJsonArray(ListEnvelope[] array) {
        JsonArray list = new JsonArray();
        for (ListEnvelope item : array) {
            list.add(item.getJson());
        }
        return list;
    }

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

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}
