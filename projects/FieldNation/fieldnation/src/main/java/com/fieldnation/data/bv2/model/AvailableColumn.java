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

public class AvailableColumn implements Parcelable {
    private static final String TAG = "AvailableColumn";

    @Json(name = "items")
    private AvailableColumnItems[] _items;

    @Json(name = "group")
    private String _group;

    public AvailableColumn() {
    }

    public void setItems(AvailableColumnItems[] items) {
        _items = items;
    }

    public AvailableColumnItems[] getItems() {
        return _items;
    }

    public AvailableColumn items(AvailableColumnItems[] items) {
        _items = items;
        return this;
    }

    public void setGroup(String group) {
        _group = group;
    }

    public String getGroup() {
        return _group;
    }

    public AvailableColumn group(String group) {
        _group = group;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static AvailableColumn fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(AvailableColumn.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(AvailableColumn availableColumn) {
        try {
            return Serializer.serializeObject(availableColumn);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<AvailableColumn> CREATOR = new Parcelable.Creator<AvailableColumn>() {

        @Override
        public AvailableColumn createFromParcel(Parcel source) {
            try {
                return AvailableColumn.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public AvailableColumn[] newArray(int size) {
            return new AvailableColumn[size];
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
