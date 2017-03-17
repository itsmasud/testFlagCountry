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
import com.fieldnation.fntools.misc;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgen from swagger.
 */

public class AvailableColumnItems implements Parcelable {
    private static final String TAG = "AvailableColumnItems";

    @Json(name = "can_sort")
    private Boolean _canSort;

    @Json(name = "group")
    private String _group;

    @Json(name = "icon")
    private String _icon;

    @Json(name = "id")
    private String _id;

    @Json(name = "label")
    private String _label;

    @Json(name = "order")
    private Integer _order;

    @Json(name = "selected")
    private Boolean _selected;

    @Json(name = "sort_dir")
    private String _sortDir;

    @Source
    private JsonObject SOURCE;

    public AvailableColumnItems() {
        SOURCE = new JsonObject();
    }

    public AvailableColumnItems(JsonObject obj) {
        SOURCE = obj;
    }

    public void setCanSort(Boolean canSort) throws ParseException {
        _canSort = canSort;
        SOURCE.put("can_sort", canSort);
    }

    public Boolean getCanSort() {
        try {
            if (_canSort == null && SOURCE.has("can_sort") && SOURCE.get("can_sort") != null)
                _canSort = SOURCE.getBoolean("can_sort");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _canSort;
    }

    public AvailableColumnItems canSort(Boolean canSort) throws ParseException {
        _canSort = canSort;
        SOURCE.put("can_sort", canSort);
        return this;
    }

    public void setGroup(String group) throws ParseException {
        _group = group;
        SOURCE.put("group", group);
    }

    public String getGroup() {
        try {
            if (_group == null && SOURCE.has("group") && SOURCE.get("group") != null)
                _group = SOURCE.getString("group");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _group;
    }

    public AvailableColumnItems group(String group) throws ParseException {
        _group = group;
        SOURCE.put("group", group);
        return this;
    }

    public void setIcon(String icon) throws ParseException {
        _icon = icon;
        SOURCE.put("icon", icon);
    }

    public String getIcon() {
        try {
            if (_icon == null && SOURCE.has("icon") && SOURCE.get("icon") != null)
                _icon = SOURCE.getString("icon");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _icon;
    }

    public AvailableColumnItems icon(String icon) throws ParseException {
        _icon = icon;
        SOURCE.put("icon", icon);
        return this;
    }

    public void setId(String id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public String getId() {
        try {
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getString("id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public AvailableColumnItems id(String id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setLabel(String label) throws ParseException {
        _label = label;
        SOURCE.put("label", label);
    }

    public String getLabel() {
        try {
            if (_label == null && SOURCE.has("label") && SOURCE.get("label") != null)
                _label = SOURCE.getString("label");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _label;
    }

    public AvailableColumnItems label(String label) throws ParseException {
        _label = label;
        SOURCE.put("label", label);
        return this;
    }

    public void setOrder(Integer order) throws ParseException {
        _order = order;
        SOURCE.put("order", order);
    }

    public Integer getOrder() {
        try {
            if (_order == null && SOURCE.has("order") && SOURCE.get("order") != null)
                _order = SOURCE.getInt("order");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _order;
    }

    public AvailableColumnItems order(Integer order) throws ParseException {
        _order = order;
        SOURCE.put("order", order);
        return this;
    }

    public void setSelected(Boolean selected) throws ParseException {
        _selected = selected;
        SOURCE.put("selected", selected);
    }

    public Boolean getSelected() {
        try {
            if (_selected == null && SOURCE.has("selected") && SOURCE.get("selected") != null)
                _selected = SOURCE.getBoolean("selected");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _selected;
    }

    public AvailableColumnItems selected(Boolean selected) throws ParseException {
        _selected = selected;
        SOURCE.put("selected", selected);
        return this;
    }

    public void setSortDir(String sortDir) throws ParseException {
        _sortDir = sortDir;
        SOURCE.put("sort_dir", sortDir);
    }

    public String getSortDir() {
        try {
            if (_sortDir == null && SOURCE.has("sort_dir") && SOURCE.get("sort_dir") != null)
                _sortDir = SOURCE.getString("sort_dir");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _sortDir;
    }

    public AvailableColumnItems sortDir(String sortDir) throws ParseException {
        _sortDir = sortDir;
        SOURCE.put("sort_dir", sortDir);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(AvailableColumnItems[] array) {
        JsonArray list = new JsonArray();
        for (AvailableColumnItems item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static AvailableColumnItems[] fromJsonArray(JsonArray array) {
        AvailableColumnItems[] list = new AvailableColumnItems[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static AvailableColumnItems fromJson(JsonObject obj) {
        try {
            return new AvailableColumnItems(obj);
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
    public static final Parcelable.Creator<AvailableColumnItems> CREATOR = new Parcelable.Creator<AvailableColumnItems>() {

        @Override
        public AvailableColumnItems createFromParcel(Parcel source) {
            try {
                return AvailableColumnItems.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public AvailableColumnItems[] newArray(int size) {
            return new AvailableColumnItems[size];
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
        return getId() != null && getId() != 0;
    }
}
