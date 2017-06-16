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

public class HoldType implements Parcelable {
    private static final String TAG = "HoldType";

    @Json(name = "description")
    private String _description;

    @Json(name = "id")
    private Integer _id;

    @Source
    private JsonObject SOURCE;

    public HoldType() {
        SOURCE = new JsonObject();
    }

    public HoldType(JsonObject obj) {
        SOURCE = obj;
    }

    public void setDescription(String description) throws ParseException {
        _description = description;
        SOURCE.put("description", description);
    }

    public String getDescription() {
        try {
            if (_description == null && SOURCE.has("description") && SOURCE.get("description") != null)
                _description = SOURCE.getString("description");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _description;
    }

    public HoldType description(String description) throws ParseException {
        _description = description;
        SOURCE.put("description", description);
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

    public HoldType id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(HoldType[] array) {
        JsonArray list = new JsonArray();
        for (HoldType item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static HoldType[] fromJsonArray(JsonArray array) {
        HoldType[] list = new HoldType[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static HoldType fromJson(JsonObject obj) {
        try {
            return new HoldType(obj);
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
    public static final Parcelable.Creator<HoldType> CREATOR = new Parcelable.Creator<HoldType>() {

        @Override
        public HoldType createFromParcel(Parcel source) {
            try {
                return HoldType.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public HoldType[] newArray(int size) {
            return new HoldType[size];
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

}
