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

public class PayTerm implements Parcelable {
    private static final String TAG = "PayTerm";

    @Json(name = "explanation")
    private String _explanation;

    @Json(name = "id")
    private String _id;

    @Source
    private JsonObject SOURCE;

    public PayTerm() {
        SOURCE = new JsonObject();
    }

    public PayTerm(JsonObject obj) {
        SOURCE = obj;
    }

    public void setExplanation(String explanation) throws ParseException {
        _explanation = explanation;
        SOURCE.put("explanation", explanation);
    }

    public String getExplanation() {
        try {
            if (_explanation == null && SOURCE.has("explanation") && SOURCE.get("explanation") != null)
                _explanation = SOURCE.getString("explanation");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _explanation;
    }

    public PayTerm explanation(String explanation) throws ParseException {
        _explanation = explanation;
        SOURCE.put("explanation", explanation);
        return this;
    }

    public void setId(String id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public String getId() {
        try {
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getString("id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public PayTerm id(String id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(PayTerm[] array) {
        JsonArray list = new JsonArray();
        for (PayTerm item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static PayTerm[] fromJsonArray(JsonArray array) {
        PayTerm[] list = new PayTerm[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static PayTerm fromJson(JsonObject obj) {
        try {
            return new PayTerm(obj);
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
    public static final Parcelable.Creator<PayTerm> CREATOR = new Parcelable.Creator<PayTerm>() {

        @Override
        public PayTerm createFromParcel(Parcel source) {
            try {
                return PayTerm.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PayTerm[] newArray(int size) {
            return new PayTerm[size];
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
