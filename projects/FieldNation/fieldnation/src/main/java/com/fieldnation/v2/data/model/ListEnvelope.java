package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
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
    private JsonObject SOURCE;

    public ListEnvelope() {
        SOURCE = new JsonObject();
    }

    public ListEnvelope(JsonObject obj) {
        SOURCE = obj;
    }

    public void setAvailableColumns(AvailableColumn[] availableColumns) throws ParseException {
        _availableColumns = availableColumns;
        SOURCE.put("available_columns", AvailableColumn.toJsonArray(availableColumns));
    }

    public AvailableColumn[] getAvailableColumns() {
        try {
            if (_availableColumns != null)
                return _availableColumns;

            if (SOURCE.has("available_columns") && SOURCE.get("available_columns") != null) {
                _availableColumns = AvailableColumn.fromJsonArray(SOURCE.getJsonArray("available_columns"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_columns == null && SOURCE.has("columns") && SOURCE.get("columns") != null)
                _columns = SOURCE.getString("columns");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_list == null && SOURCE.has("list") && SOURCE.get("list") != null)
                _list = SOURCE.getString("list");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_order == null && SOURCE.has("order") && SOURCE.get("order") != null)
                _order = OrderEnum.fromString(SOURCE.getString("order"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_page == null && SOURCE.has("page") && SOURCE.get("page") != null)
                _page = SOURCE.getInt("page");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_pages == null && SOURCE.has("pages") && SOURCE.get("pages") != null)
                _pages = SOURCE.getInt("pages");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_perPage == null && SOURCE.has("per_page") && SOURCE.get("per_page") != null)
                _perPage = SOURCE.getInt("per_page");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_sort == null && SOURCE.has("sort") && SOURCE.get("sort") != null)
                _sort = SOURCE.getString("sort");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_total == null && SOURCE.has("total") && SOURCE.get("total") != null)
                _total = SOURCE.getInt("total");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_view == null && SOURCE.has("view") && SOURCE.get("view") != null)
                _view = ViewEnum.fromString(SOURCE.getString("view"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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

        public static ViewEnum fromString(String value) {
            ViewEnum[] values = values();
            for (ViewEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static ViewEnum[] fromJsonArray(JsonArray jsonArray) {
            ViewEnum[] list = new ViewEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
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

        public static OrderEnum fromString(String value) {
            OrderEnum[] values = values();
            for (OrderEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static OrderEnum[] fromJsonArray(JsonArray jsonArray) {
            OrderEnum[] list = new OrderEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
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
            return new ListEnvelope(obj);
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

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return true;
    }
}
