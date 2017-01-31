package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class Problems implements Parcelable {
    private static final String TAG = "Problems";

    @Json(name = "next")
    private ProblemsNext[] _next;

    @Json(name = "reason")
    private String _reason;

    @Json(name = "problem_id")
    private Integer _problemId;

    public Problems() {
    }

    public void setNext(ProblemsNext[] next) {
        _next = next;
    }

    public ProblemsNext[] getNext() {
        return _next;
    }

    public Problems next(ProblemsNext[] next) {
        _next = next;
        return this;
    }

    public void setReason(String reason) {
        _reason = reason;
    }

    public String getReason() {
        return _reason;
    }

    public Problems reason(String reason) {
        _reason = reason;
        return this;
    }

    public void setProblemId(Integer problemId) {
        _problemId = problemId;
    }

    public Integer getProblemId() {
        return _problemId;
    }

    public Problems problemId(Integer problemId) {
        _problemId = problemId;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Problems[] fromJsonArray(JsonArray array) {
        Problems[] list = new Problems[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Problems fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Problems.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Problems problems) {
        try {
            return Serializer.serializeObject(problems);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Problems> CREATOR = new Parcelable.Creator<Problems>() {

        @Override
        public Problems createFromParcel(Parcel source) {
            try {
                return Problems.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Problems[] newArray(int size) {
            return new Problems[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(toJson(), flags);
    }
}
