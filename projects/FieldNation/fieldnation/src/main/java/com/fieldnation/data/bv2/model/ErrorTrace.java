package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class ErrorTrace implements Parcelable {
    private static final String TAG = "ErrorTrace";

    @Json(name = "args")
    private ErrorTraceArgs[] _args;

    @Json(name = "file")
    private String _file;

    @Json(name = "line")
    private Integer _line;

    @Json(name = "function")
    private String _function;

    @Json(name = "type")
    private String _type;

    @Json(name = "class")
    private String _class;

    @Json(name = "object")
    private ErrorTraceObject _object;

    public ErrorTrace() {
    }

    public void setArgs(ErrorTraceArgs[] args) {
        _args = args;
    }

    public ErrorTraceArgs[] getArgs() {
        return _args;
    }

    public ErrorTrace args(ErrorTraceArgs[] args) {
        _args = args;
        return this;
    }

    public void setFile(String file) {
        _file = file;
    }

    public String getFile() {
        return _file;
    }

    public ErrorTrace file(String file) {
        _file = file;
        return this;
    }

    public void setLine(Integer line) {
        _line = line;
    }

    public Integer getLine() {
        return _line;
    }

    public ErrorTrace line(Integer line) {
        _line = line;
        return this;
    }

    public void setFunction(String function) {
        _function = function;
    }

    public String getFunction() {
        return _function;
    }

    public ErrorTrace function(String function) {
        _function = function;
        return this;
    }

    public void setType(String type) {
        _type = type;
    }

    public String getType() {
        return _type;
    }

    public ErrorTrace type(String type) {
        _type = type;
        return this;
    }

    public void setClass(String class) {
        _class = class;
    }

    public String getClass() {
        return _class;
    }

    public ErrorTrace class(String class) {
        _class = class;
        return this;
    }

    public void setObject(ErrorTraceObject object) {
        _object = object;
    }

    public ErrorTraceObject getObject() {
        return _object;
    }

    public ErrorTrace object(ErrorTraceObject object) {
        _object = object;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ErrorTrace fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ErrorTrace.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ErrorTrace errorTrace) {
        try {
            return Serializer.serializeObject(errorTrace);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<ErrorTrace> CREATOR = new Parcelable.Creator<ErrorTrace>() {

        @Override
        public ErrorTrace createFromParcel(Parcel source) {
            try {
                return ErrorTrace.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ErrorTrace[] newArray(int size) {
            return new ErrorTrace[size];
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
