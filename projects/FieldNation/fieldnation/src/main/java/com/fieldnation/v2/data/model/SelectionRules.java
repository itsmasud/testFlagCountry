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

public class SelectionRules implements Parcelable {
    private static final String TAG = "SelectionRules";

    @Source
    private JsonObject SOURCE;

    public SelectionRules() {
        SOURCE = new JsonObject();
    }

    public SelectionRules(JsonObject obj) {
        SOURCE = obj;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(SelectionRules[] array) {
        JsonArray list = new JsonArray();
        for (SelectionRules item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static SelectionRules[] fromJsonArray(JsonArray array) {
        SelectionRules[] list = new SelectionRules[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static SelectionRules fromJson(JsonObject obj) {
        try {
            return new SelectionRules(obj);
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
    public static final Parcelable.Creator<SelectionRules> CREATOR = new Parcelable.Creator<SelectionRules>() {

        @Override
        public SelectionRules createFromParcel(Parcel source) {
            try {
                return SelectionRules.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public SelectionRules[] newArray(int size) {
            return new SelectionRules[size];
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
