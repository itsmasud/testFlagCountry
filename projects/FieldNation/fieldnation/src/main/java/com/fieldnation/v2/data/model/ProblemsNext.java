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

public class ProblemsNext implements Parcelable {
    private static final String TAG = "ProblemsNext";

    @Json(name = "problem_id")
    private Integer _problemId;

    @Json(name = "reason")
    private String _reason;

    @Source
    private JsonObject SOURCE;

    public ProblemsNext() {
        SOURCE = new JsonObject();
    }

    public ProblemsNext(JsonObject obj) {
        SOURCE = obj;
    }

    public void setProblemId(Integer problemId) throws ParseException {
        _problemId = problemId;
        SOURCE.put("problem_id", problemId);
    }

    public Integer getProblemId() {
        try {
            if (_problemId != null)
                return _problemId;

            if (SOURCE.has("problem_id") && SOURCE.get("problem_id") != null)
                _problemId = SOURCE.getInt("problem_id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _problemId;
    }

    public ProblemsNext problemId(Integer problemId) throws ParseException {
        _problemId = problemId;
        SOURCE.put("problem_id", problemId);
        return this;
    }

    public void setReason(String reason) throws ParseException {
        _reason = reason;
        SOURCE.put("reason", reason);
    }

    public String getReason() {
        try {
            if (_reason != null)
                return _reason;

            if (SOURCE.has("reason") && SOURCE.get("reason") != null)
                _reason = SOURCE.getString("reason");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _reason;
    }

    public ProblemsNext reason(String reason) throws ParseException {
        _reason = reason;
        SOURCE.put("reason", reason);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(ProblemsNext[] array) {
        JsonArray list = new JsonArray();
        for (ProblemsNext item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static ProblemsNext[] fromJsonArray(JsonArray array) {
        ProblemsNext[] list = new ProblemsNext[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ProblemsNext fromJson(JsonObject obj) {
        try {
            return new ProblemsNext(obj);
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
    public static final Parcelable.Creator<ProblemsNext> CREATOR = new Parcelable.Creator<ProblemsNext>() {

        @Override
        public ProblemsNext createFromParcel(Parcel source) {
            try {
                return ProblemsNext.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ProblemsNext[] newArray(int size) {
            return new ProblemsNext[size];
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
