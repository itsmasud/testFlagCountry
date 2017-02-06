package com.fieldnation.v2.data.model;

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

public class ProblemsNext implements Parcelable {
    private static final String TAG = "ProblemsNext";

    @Json(name = "reason")
    private String _reason;

    @Json(name = "problem_id")
    private Integer _problemId;

    public ProblemsNext() {
    }

    public void setReason(String reason) {
        _reason = reason;
    }

    public String getReason() {
        return _reason;
    }

    public ProblemsNext reason(String reason) {
        _reason = reason;
        return this;
    }

    public void setProblemId(Integer problemId) {
        _problemId = problemId;
    }

    public Integer getProblemId() {
        return _problemId;
    }

    public ProblemsNext problemId(Integer problemId) {
        _problemId = problemId;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ProblemsNext[] fromJsonArray(JsonArray array) {
        ProblemsNext[] list = new ProblemsNext[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ProblemsNext fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ProblemsNext.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ProblemsNext problemsNext) {
        try {
            return Serializer.serializeObject(problemsNext);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
