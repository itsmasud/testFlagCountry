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

public class ErrorTrace implements Parcelable {
    private static final String TAG = "ErrorTrace";

    @Json(name = "args")
    private ErrorTraceArgs[] _args;

    @Json(name = "class")
    private String _class;

    @Json(name = "file")
    private String _file;

    @Json(name = "function")
    private String _function;

    @Json(name = "line")
    private Integer _line;

    @Json(name = "object")
    private ErrorTraceObject _object;

    @Json(name = "type")
    private String _type;

    @Source
    private JsonObject SOURCE;

    public ErrorTrace() {
        SOURCE = new JsonObject();
    }

    public ErrorTrace(JsonObject obj) {
        SOURCE = obj;
    }

    public void setArgs(ErrorTraceArgs[] args) throws ParseException {
        _args = args;
        SOURCE.put("args", ErrorTraceArgs.toJsonArray(args));
    }

    public ErrorTraceArgs[] getArgs() {
        try {
            if (_args != null)
                return _args;

            if (SOURCE.has("args") && SOURCE.get("args") != null) {
                _args = ErrorTraceArgs.fromJsonArray(SOURCE.getJsonArray("args"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _args;
    }

    public ErrorTrace args(ErrorTraceArgs[] args) throws ParseException {
        _args = args;
        SOURCE.put("args", ErrorTraceArgs.toJsonArray(args), true);
        return this;
    }

    public void setClass(String clazz) throws ParseException {
        _class = clazz;
        SOURCE.put("class", clazz);
    }

    public String getClazz() {
        try {
            if (_class != null)
                return _class;

            if (SOURCE.has("class") && SOURCE.get("class") != null)
                _class = SOURCE.getString("class");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _class;
    }

    public ErrorTrace clazz(String clazz) throws ParseException {
        _class = clazz;
        SOURCE.put("class", clazz);
        return this;
    }

    public void setFile(String file) throws ParseException {
        _file = file;
        SOURCE.put("file", file);
    }

    public String getFile() {
        try {
            if (_file != null)
                return _file;

            if (SOURCE.has("file") && SOURCE.get("file") != null)
                _file = SOURCE.getString("file");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _file;
    }

    public ErrorTrace file(String file) throws ParseException {
        _file = file;
        SOURCE.put("file", file);
        return this;
    }

    public void setFunction(String function) throws ParseException {
        _function = function;
        SOURCE.put("function", function);
    }

    public String getFunction() {
        try {
            if (_function != null)
                return _function;

            if (SOURCE.has("function") && SOURCE.get("function") != null)
                _function = SOURCE.getString("function");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _function;
    }

    public ErrorTrace function(String function) throws ParseException {
        _function = function;
        SOURCE.put("function", function);
        return this;
    }

    public void setLine(Integer line) throws ParseException {
        _line = line;
        SOURCE.put("line", line);
    }

    public Integer getLine() {
        try {
            if (_line != null)
                return _line;

            if (SOURCE.has("line") && SOURCE.get("line") != null)
                _line = SOURCE.getInt("line");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _line;
    }

    public ErrorTrace line(Integer line) throws ParseException {
        _line = line;
        SOURCE.put("line", line);
        return this;
    }

    public void setObject(ErrorTraceObject object) throws ParseException {
        _object = object;
        SOURCE.put("object", object.getJson());
    }

    public ErrorTraceObject getObject() {
        try {
            if (_object != null)
                return _object;

            if (SOURCE.has("object") && SOURCE.get("object") != null)
                _object = ErrorTraceObject.fromJson(SOURCE.getJsonObject("object"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _object;
    }

    public ErrorTrace object(ErrorTraceObject object) throws ParseException {
        _object = object;
        SOURCE.put("object", object.getJson());
        return this;
    }

    public void setType(String type) throws ParseException {
        _type = type;
        SOURCE.put("type", type);
    }

    public String getType() {
        try {
            if (_type != null)
                return _type;

            if (SOURCE.has("type") && SOURCE.get("type") != null)
                _type = SOURCE.getString("type");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _type;
    }

    public ErrorTrace type(String type) throws ParseException {
        _type = type;
        SOURCE.put("type", type);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(ErrorTrace[] array) {
        JsonArray list = new JsonArray();
        for (ErrorTrace item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static ErrorTrace[] fromJsonArray(JsonArray array) {
        ErrorTrace[] list = new ErrorTrace[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ErrorTrace fromJson(JsonObject obj) {
        try {
            return new ErrorTrace(obj);
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
        dest.writeParcelable(getJson(), flags);
    }
}
