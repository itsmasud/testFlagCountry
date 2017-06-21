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

public class CheckInOutTimeLog implements Parcelable {
    private static final String TAG = "CheckInOutTimeLog";

    @Json(name = "id")
    private Integer _id;

    @Source
    private JsonObject SOURCE;

    public CheckInOutTimeLog() {
        SOURCE = new JsonObject();
    }

    public CheckInOutTimeLog(JsonObject obj) {
        SOURCE = obj;
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

    public CheckInOutTimeLog id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(CheckInOutTimeLog[] array) {
        JsonArray list = new JsonArray();
        for (CheckInOutTimeLog item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static CheckInOutTimeLog[] fromJsonArray(JsonArray array) {
        CheckInOutTimeLog[] list = new CheckInOutTimeLog[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static CheckInOutTimeLog fromJson(JsonObject obj) {
        try {
            return new CheckInOutTimeLog(obj);
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
    public static final Parcelable.Creator<CheckInOutTimeLog> CREATOR = new Parcelable.Creator<CheckInOutTimeLog>() {

        @Override
        public CheckInOutTimeLog createFromParcel(Parcel source) {
            try {
                return CheckInOutTimeLog.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public CheckInOutTimeLog[] newArray(int size) {
            return new CheckInOutTimeLog[size];
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
