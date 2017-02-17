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

public class Robocall implements Parcelable {
    private static final String TAG = "Robocall";

    @Json(name = "id")
    private Integer _id;

    @Json(name = "label")
    private String _label;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public Robocall() {
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        return _id;
    }

    public Robocall id(Integer id) throws ParseException {
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

    public Robocall label(String label) throws ParseException {
        _label = label;
        SOURCE.put("label", label);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Robocall[] array) {
        JsonArray list = new JsonArray();
        for (Robocall item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Robocall[] fromJsonArray(JsonArray array) {
        Robocall[] list = new Robocall[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Robocall fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Robocall.class, obj);
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
    public static final Parcelable.Creator<Robocall> CREATOR = new Parcelable.Creator<Robocall>() {

        @Override
        public Robocall createFromParcel(Parcel source) {
            try {
                return Robocall.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Robocall[] newArray(int size) {
            return new Robocall[size];
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
