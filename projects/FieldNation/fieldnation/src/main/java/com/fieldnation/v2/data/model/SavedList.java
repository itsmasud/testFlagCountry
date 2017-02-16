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

public class SavedList implements Parcelable {
    private static final String TAG = "SavedList";

    @Json(name = "columns")
    private String _columns;

    @Json(name = "count")
    private Integer _count;

    @Json(name = "default")
    private Boolean _default;

    @Json(name = "id")
    private String _id;

    @Json(name = "label")
    private String _label;

    @Json(name = "order")
    private OrderEnum _order;

    @Json(name = "sort")
    private String _sort;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public SavedList() {
    }

    public void setColumns(String columns) throws ParseException {
        _columns = columns;
        SOURCE.put("columns", columns);
    }

    public String getColumns() {
        return _columns;
    }

    public SavedList columns(String columns) throws ParseException {
        _columns = columns;
        SOURCE.put("columns", columns);
        return this;
    }

    public void setCount(Integer count) throws ParseException {
        _count = count;
        SOURCE.put("count", count);
    }

    public Integer getCount() {
        return _count;
    }

    public SavedList count(Integer count) throws ParseException {
        _count = count;
        SOURCE.put("count", count);
        return this;
    }

    public void setDefault(Boolean defaultt) throws ParseException {
        _default = defaultt;
        SOURCE.put("default", defaultt);
    }

    public Boolean getDefault() {
        return _default;
    }

    public SavedList defaultt(Boolean defaultt) throws ParseException {
        _default = defaultt;
        SOURCE.put("default", defaultt);
        return this;
    }

    public void setId(String id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public String getId() {
        return _id;
    }

    public SavedList id(String id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setLabel(String label) throws ParseException {
        _label = label;
        SOURCE.put("label", label);
    }

    public String getLabel() {
        return _label;
    }

    public SavedList label(String label) throws ParseException {
        _label = label;
        SOURCE.put("label", label);
        return this;
    }

    public void setOrder(OrderEnum order) throws ParseException {
        _order = order;
        SOURCE.put("order", order.toString());
    }

    public OrderEnum getOrder() {
        return _order;
    }

    public SavedList order(OrderEnum order) throws ParseException {
        _order = order;
        SOURCE.put("order", order.toString());
        return this;
    }

    public void setSort(String sort) throws ParseException {
        _sort = sort;
        SOURCE.put("sort", sort);
    }

    public String getSort() {
        return _sort;
    }

    public SavedList sort(String sort) throws ParseException {
        _sort = sort;
        SOURCE.put("sort", sort);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
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
    public static JsonArray toJsonArray(SavedList[] array) {
        JsonArray list = new JsonArray();
        for (SavedList item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static SavedList[] fromJsonArray(JsonArray array) {
        SavedList[] list = new SavedList[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static SavedList fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(SavedList.class, obj);
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
    public static final Parcelable.Creator<SavedList> CREATOR = new Parcelable.Creator<SavedList>() {

        @Override
        public SavedList createFromParcel(Parcel source) {
            try {
                return SavedList.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public SavedList[] newArray(int size) {
            return new SavedList[size];
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
    public String getTitle() {
        if (getCount() == null) {
            return getLabel();
        }
        return getLabel() + " (" + getCount() + ")";
    }
}
