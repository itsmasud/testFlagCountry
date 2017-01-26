package com.fieldnation.data.bv2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class AvailableColumnItems implements Parcelable {
    private static final String TAG = "AvailableColumnItems";

    @Json(name = "icon")
    private String _icon;

    @Json(name = "id")
    private String _id;

    @Json(name = "label")
    private String _label;

    @Json(name = "can_sort")
    private Boolean _canSort;

    @Json(name = "selected")
    private Boolean _selected;

    @Json(name = "sort_dir")
    private String _sortDir;

    @Json(name = "group")
    private String _group;

    @Json(name = "order")
    private Integer _order;

    public AvailableColumnItems() {
    }

    public void setIcon(String icon) {
        _icon = icon;
    }

    public String getIcon() {
        return _icon;
    }

    public AvailableColumnItems icon(String icon) {
        _icon = icon;
        return this;
    }

    public void setId(String id) {
        _id = id;
    }

    public String getId() {
        return _id;
    }

    public AvailableColumnItems id(String id) {
        _id = id;
        return this;
    }

    public void setLabel(String label) {
        _label = label;
    }

    public String getLabel() {
        return _label;
    }

    public AvailableColumnItems label(String label) {
        _label = label;
        return this;
    }

    public void setCanSort(Boolean canSort) {
        _canSort = canSort;
    }

    public Boolean getCanSort() {
        return _canSort;
    }

    public AvailableColumnItems canSort(Boolean canSort) {
        _canSort = canSort;
        return this;
    }

    public void setSelected(Boolean selected) {
        _selected = selected;
    }

    public Boolean getSelected() {
        return _selected;
    }

    public AvailableColumnItems selected(Boolean selected) {
        _selected = selected;
        return this;
    }

    public void setSortDir(String sortDir) {
        _sortDir = sortDir;
    }

    public String getSortDir() {
        return _sortDir;
    }

    public AvailableColumnItems sortDir(String sortDir) {
        _sortDir = sortDir;
        return this;
    }

    public void setGroup(String group) {
        _group = group;
    }

    public String getGroup() {
        return _group;
    }

    public AvailableColumnItems group(String group) {
        _group = group;
        return this;
    }

    public void setOrder(Integer order) {
        _order = order;
    }

    public Integer getOrder() {
        return _order;
    }

    public AvailableColumnItems order(Integer order) {
        _order = order;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static AvailableColumnItems fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(AvailableColumnItems.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
