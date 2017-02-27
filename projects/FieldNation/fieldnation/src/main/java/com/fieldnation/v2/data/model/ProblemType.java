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

public class ProblemType implements Parcelable {
    private static final String TAG = "ProblemType";

    @Json(name = "children")
    private ProblemType[] _children;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "name")
    private String _name;

    @Json(name = "other")
    private String _other;

    @Json(name = "selectable")
    private Boolean _selectable;

    @Source
    private JsonObject SOURCE;

    public ProblemType() {
        SOURCE = new JsonObject();
    }

    public ProblemType(JsonObject obj) {
        SOURCE = obj;
    }

    public void setChildren(ProblemType[] children) throws ParseException {
        _children = children;
        SOURCE.put("children", ProblemType.toJsonArray(children));
    }

    public ProblemType[] getChildren() {
        try {
            if (_children != null)
                return _children;

            if (SOURCE.has("children") && SOURCE.get("children") != null) {
                _children = ProblemType.fromJsonArray(SOURCE.getJsonArray("children"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _children;
    }

    public ProblemType children(ProblemType[] children) throws ParseException {
        _children = children;
        SOURCE.put("children", ProblemType.toJsonArray(children), true);
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        try {
            if (_id != null)
                return _id;

            if (SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public ProblemType id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setName(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
    }

    public String getName() {
        try {
            if (_name != null)
                return _name;

            if (SOURCE.has("name") && SOURCE.get("name") != null)
                _name = SOURCE.getString("name");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _name;
    }

    public ProblemType name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    public void setOther(String other) throws ParseException {
        _other = other;
        SOURCE.put("other", other);
    }

    public String getOther() {
        try {
            if (_other != null)
                return _other;

            if (SOURCE.has("other") && SOURCE.get("other") != null)
                _other = SOURCE.getString("other");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _other;
    }

    public ProblemType other(String other) throws ParseException {
        _other = other;
        SOURCE.put("other", other);
        return this;
    }

    public void setSelectable(Boolean selectable) throws ParseException {
        _selectable = selectable;
        SOURCE.put("selectable", selectable);
    }

    public Boolean getSelectable() {
        try {
            if (_selectable != null)
                return _selectable;

            if (SOURCE.has("selectable") && SOURCE.get("selectable") != null)
                _selectable = SOURCE.getBoolean("selectable");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _selectable;
    }

    public ProblemType selectable(Boolean selectable) throws ParseException {
        _selectable = selectable;
        SOURCE.put("selectable", selectable);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(ProblemType[] array) {
        JsonArray list = new JsonArray();
        for (ProblemType item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static ProblemType[] fromJsonArray(JsonArray array) {
        ProblemType[] list = new ProblemType[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ProblemType fromJson(JsonObject obj) {
        try {
            return new ProblemType(obj);
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
    public static final Parcelable.Creator<ProblemType> CREATOR = new Parcelable.Creator<ProblemType>() {

        @Override
        public ProblemType createFromParcel(Parcel source) {
            try {
                return ProblemType.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ProblemType[] newArray(int size) {
            return new ProblemType[size];
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
