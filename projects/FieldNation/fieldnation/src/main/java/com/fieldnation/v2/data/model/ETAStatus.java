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

public class ETAStatus implements Parcelable {
    private static final String TAG = "ETAStatus";

    @Json(name = "condition")
    private Condition _condition;

    @Json(name = "name")
    private NameEnum _name;

    @Json(name = "updated")
    private Date _updated;

    @Source
    private JsonObject SOURCE;

    public ETAStatus() {
        SOURCE = new JsonObject();
    }

    public ETAStatus(JsonObject obj) {
        SOURCE = obj;
    }

    public void setCondition(Condition condition) throws ParseException {
        _condition = condition;
        SOURCE.put("condition", condition.getJson());
    }

    public Condition getCondition() {
        try {
            if (_condition == null && SOURCE.has("condition") && SOURCE.get("condition") != null)
                _condition = Condition.fromJson(SOURCE.getJsonObject("condition"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_condition != null && _condition.isSet())
            return _condition;

        return null;
    }

    public ETAStatus condition(Condition condition) throws ParseException {
        _condition = condition;
        SOURCE.put("condition", condition.getJson());
        return this;
    }

    public void setName(NameEnum name) throws ParseException {
        _name = name;
        SOURCE.put("name", name.toString());
    }

    public NameEnum getName() {
        try {
            if (_name == null && SOURCE.has("name") && SOURCE.get("name") != null)
                _name = NameEnum.fromString(SOURCE.getString("name"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _name;
    }

    public ETAStatus name(NameEnum name) throws ParseException {
        _name = name;
        SOURCE.put("name", name.toString());
        return this;
    }

    public void setUpdated(Date updated) throws ParseException {
        _updated = updated;
        SOURCE.put("updated", updated.getJson());
    }

    public Date getUpdated() {
        try {
            if (_updated == null && SOURCE.has("updated") && SOURCE.get("updated") != null)
                _updated = Date.fromJson(SOURCE.getJsonObject("updated"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_updated != null && _updated.isSet())
            return _updated;

        return null;
    }

    public ETAStatus updated(Date updated) throws ParseException {
        _updated = updated;
        SOURCE.put("updated", updated.getJson());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum NameEnum {
        @Json(name = "confirmed")
        CONFIRMED("confirmed"),
        @Json(name = "onmyway")
        ONMYWAY("onmyway"),
        @Json(name = "readytogo")
        READYTOGO("readytogo"),
        @Json(name = "set")
        SET("set"),
        @Json(name = "unconfirmed")
        UNCONFIRMED("unconfirmed");

        private String value;

        NameEnum(String value) {
            this.value = value;
        }

        public static NameEnum fromString(String value) {
            NameEnum[] values = values();
            for (NameEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static NameEnum[] fromJsonArray(JsonArray jsonArray) {
            NameEnum[] list = new NameEnum[jsonArray.size()];
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
    public static JsonArray toJsonArray(ETAStatus[] array) {
        JsonArray list = new JsonArray();
        for (ETAStatus item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static ETAStatus[] fromJsonArray(JsonArray array) {
        ETAStatus[] list = new ETAStatus[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ETAStatus fromJson(JsonObject obj) {
        try {
            return new ETAStatus(obj);
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
    public static final Parcelable.Creator<ETAStatus> CREATOR = new Parcelable.Creator<ETAStatus>() {

        @Override
        public ETAStatus createFromParcel(Parcel source) {
            try {
                return ETAStatus.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ETAStatus[] newArray(int size) {
            return new ETAStatus[size];
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
