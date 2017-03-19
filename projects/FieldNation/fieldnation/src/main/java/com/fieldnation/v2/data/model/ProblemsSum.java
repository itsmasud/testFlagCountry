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

public class ProblemsSum implements Parcelable {
    private static final String TAG = "ProblemsSum";

    @Json(name = "resolved")
    private Integer _resolved;

    @Json(name = "unresolved")
    private Integer _unresolved;

    @Source
    private JsonObject SOURCE;

    public ProblemsSum() {
        SOURCE = new JsonObject();
    }

    public ProblemsSum(JsonObject obj) {
        SOURCE = obj;
    }

    public void setResolved(Integer resolved) throws ParseException {
        _resolved = resolved;
        SOURCE.put("resolved", resolved);
    }

    public Integer getResolved() {
        try {
            if (_resolved == null && SOURCE.has("resolved") && SOURCE.get("resolved") != null)
                _resolved = SOURCE.getInt("resolved");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _resolved;
    }

    public ProblemsSum resolved(Integer resolved) throws ParseException {
        _resolved = resolved;
        SOURCE.put("resolved", resolved);
        return this;
    }

    public void setUnresolved(Integer unresolved) throws ParseException {
        _unresolved = unresolved;
        SOURCE.put("unresolved", unresolved);
    }

    public Integer getUnresolved() {
        try {
            if (_unresolved == null && SOURCE.has("unresolved") && SOURCE.get("unresolved") != null)
                _unresolved = SOURCE.getInt("unresolved");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _unresolved;
    }

    public ProblemsSum unresolved(Integer unresolved) throws ParseException {
        _unresolved = unresolved;
        SOURCE.put("unresolved", unresolved);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(ProblemsSum[] array) {
        JsonArray list = new JsonArray();
        for (ProblemsSum item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static ProblemsSum[] fromJsonArray(JsonArray array) {
        ProblemsSum[] list = new ProblemsSum[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ProblemsSum fromJson(JsonObject obj) {
        try {
            return new ProblemsSum(obj);
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
    public static final Parcelable.Creator<ProblemsSum> CREATOR = new Parcelable.Creator<ProblemsSum>() {

        @Override
        public ProblemsSum createFromParcel(Parcel source) {
            try {
                return ProblemsSum.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ProblemsSum[] newArray(int size) {
            return new ProblemsSum[size];
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
