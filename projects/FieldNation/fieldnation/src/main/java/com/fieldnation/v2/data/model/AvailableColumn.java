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

public class AvailableColumn implements Parcelable {
    private static final String TAG = "AvailableColumn";

    @Json(name = "group")
    private String _group;

    @Json(name = "items")
    private AvailableColumnItems[] _items;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public AvailableColumn() {
    }

    public void setGroup(String group) throws ParseException {
        _group = group;
        SOURCE.put("group", group);
    }

    public String getGroup() {
        return _group;
    }

    public AvailableColumn group(String group) throws ParseException {
        _group = group;
        SOURCE.put("group", group);
        return this;
    }

    public void setItems(AvailableColumnItems[] items) throws ParseException {
        _items = items;
        SOURCE.put("items", AvailableColumnItems.toJsonArray(items));
    }

    public AvailableColumnItems[] getItems() {
        return _items;
    }

    public AvailableColumn items(AvailableColumnItems[] items) throws ParseException {
        _items = items;
        SOURCE.put("items", AvailableColumnItems.toJsonArray(items), true);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(AvailableColumn[] array) {
        JsonArray list = new JsonArray();
        for (AvailableColumn item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static AvailableColumn[] fromJsonArray(JsonArray array) {
        AvailableColumn[] list = new AvailableColumn[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static AvailableColumn fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(AvailableColumn.class, obj);
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
        dest.writeParcelable(getJson(), flags);
    }
}
