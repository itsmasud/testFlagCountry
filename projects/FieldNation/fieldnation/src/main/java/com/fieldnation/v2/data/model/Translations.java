package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger.
 */

public class Translations implements Parcelable {
    private static final String TAG = "Translations";

    @Source
    private JsonObject SOURCE;

    public Translations() {
        SOURCE = new JsonObject();
    }

    public Translations(JsonObject obj) {
        SOURCE = obj;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Translations[] array) {
        JsonArray list = new JsonArray();
        for (Translations item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Translations[] fromJsonArray(JsonArray array) {
        Translations[] list = new Translations[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Translations fromJson(JsonObject obj) {
        try {
            return new Translations(obj);
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
    public static final Creator<Translations> CREATOR = new Creator<Translations>() {

        @Override
        public Translations createFromParcel(Parcel source) {
            try {
                return Translations.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Translations[] newArray(int size) {
            return new Translations[size];
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
