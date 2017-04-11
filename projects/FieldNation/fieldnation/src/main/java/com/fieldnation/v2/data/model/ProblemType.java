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

public class ProblemType implements Parcelable {
    private static final String TAG = "ProblemType";

    @Json(name = "children")
    private ProblemType[] _children;

    @Json(name = "file_with_support")
    private Boolean _fileWithSupport;

    @Json(name = "has_other")
    private Boolean _hasOther;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "name")
    private String _name;

    @Json(name = "other")
    private String _other;

    @Json(name = "performance_event")
    private Boolean _performanceEvent;

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

    public void setFileWithSupport(Boolean fileWithSupport) throws ParseException {
        _fileWithSupport = fileWithSupport;
        SOURCE.put("file_with_support", fileWithSupport);
    }

    public Boolean getFileWithSupport() {
        try {
            if (_fileWithSupport == null && SOURCE.has("file_with_support") && SOURCE.get("file_with_support") != null)
                _fileWithSupport = SOURCE.getBoolean("file_with_support");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _fileWithSupport;
    }

    public ProblemType fileWithSupport(Boolean fileWithSupport) throws ParseException {
        _fileWithSupport = fileWithSupport;
        SOURCE.put("file_with_support", fileWithSupport);
        return this;
    }

    public void setHasOther(Boolean hasOther) throws ParseException {
        _hasOther = hasOther;
        SOURCE.put("has_other", hasOther);
    }

    public Boolean getHasOther() {
        try {
            if (_hasOther == null && SOURCE.has("has_other") && SOURCE.get("has_other") != null)
                _hasOther = SOURCE.getBoolean("has_other");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _hasOther;
    }

    public ProblemType hasOther(Boolean hasOther) throws ParseException {
        _hasOther = hasOther;
        SOURCE.put("has_other", hasOther);
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        try {
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
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
            if (_name == null && SOURCE.has("name") && SOURCE.get("name") != null)
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
            if (_other == null && SOURCE.has("other") && SOURCE.get("other") != null)
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

    public void setPerformanceEvent(Boolean performanceEvent) throws ParseException {
        _performanceEvent = performanceEvent;
        SOURCE.put("performance_event", performanceEvent);
    }

    public Boolean getPerformanceEvent() {
        try {
            if (_performanceEvent == null && SOURCE.has("performance_event") && SOURCE.get("performance_event") != null)
                _performanceEvent = SOURCE.getBoolean("performance_event");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _performanceEvent;
    }

    public ProblemType performanceEvent(Boolean performanceEvent) throws ParseException {
        _performanceEvent = performanceEvent;
        SOURCE.put("performance_event", performanceEvent);
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
            Log.v(TAG, ex);
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

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return getId() != null && getId() != 0;
    }
}
