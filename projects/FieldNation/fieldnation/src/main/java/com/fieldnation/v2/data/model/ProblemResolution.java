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

public class ProblemResolution implements Parcelable {
    private static final String TAG = "ProblemResolution";

    @Json(name = "allowed_resolvers")
    private AllowedResolversEnum[] _allowedResolvers;

    @Json(name = "at")
    private Date _at;

    @Json(name = "by")
    private User _by;

    @Json(name = "status")
    private StatusEnum _status;

    @Source
    private JsonObject SOURCE;

    public ProblemResolution() {
        SOURCE = new JsonObject();
    }

    public ProblemResolution(JsonObject obj) {
        SOURCE = obj;
    }

    public void setAllowedResolvers(AllowedResolversEnum[] allowedResolvers) throws ParseException {
        _allowedResolvers = allowedResolvers;
        JsonArray ja = new JsonArray();
        for (AllowedResolversEnum item : allowedResolvers) {
            ja.add(item.toString());
        }
        SOURCE.put("allowed_resolvers", ja);
    }

    public AllowedResolversEnum[] getAllowedResolvers() {
        try {
            if (_allowedResolvers != null)
                return _allowedResolvers;

            if (SOURCE.has("allowed_resolvers") && SOURCE.get("allowed_resolvers") != null) {
                _allowedResolvers = AllowedResolversEnum.fromJsonArray(SOURCE.getJsonArray("allowed_resolvers"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_allowedResolvers == null)
            _allowedResolvers = new AllowedResolversEnum[0];

        return _allowedResolvers;
    }

    public ProblemResolution allowedResolvers(AllowedResolversEnum[] allowedResolvers) throws ParseException {
        _allowedResolvers = allowedResolvers;
        JsonArray ja = new JsonArray();
        for (AllowedResolversEnum item : allowedResolvers) {
            ja.add(item.toString());
        }
        SOURCE.put("allowed_resolvers", ja, true);
        return this;
    }

    public void setAt(Date at) throws ParseException {
        _at = at;
        SOURCE.put("at", at.getJson());
    }

    public Date getAt() {
        try {
            if (_at == null && SOURCE.has("at") && SOURCE.get("at") != null)
                _at = Date.fromJson(SOURCE.getJsonObject("at"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_at == null)
            _at = new Date();

        return _at;
    }

    public ProblemResolution at(Date at) throws ParseException {
        _at = at;
        SOURCE.put("at", at.getJson());
        return this;
    }

    public void setBy(User by) throws ParseException {
        _by = by;
        SOURCE.put("by", by.getJson());
    }

    public User getBy() {
        try {
            if (_by == null && SOURCE.has("by") && SOURCE.get("by") != null)
                _by = User.fromJson(SOURCE.getJsonObject("by"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_by == null)
            _by = new User();

        return _by;
    }

    public ProblemResolution by(User by) throws ParseException {
        _by = by;
        SOURCE.put("by", by.getJson());
        return this;
    }

    public void setStatus(StatusEnum status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.toString());
    }

    public StatusEnum getStatus() {
        try {
            if (_status == null && SOURCE.has("status") && SOURCE.get("status") != null)
                _status = StatusEnum.fromString(SOURCE.getString("status"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _status;
    }

    public ProblemResolution status(StatusEnum status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.toString());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum StatusEnum {
        @Json(name = "open")
        OPEN("open"),
        @Json(name = "resolved")
        RESOLVED("resolved");

        private String value;

        StatusEnum(String value) {
            this.value = value;
        }

        public static StatusEnum fromString(String value) {
            StatusEnum[] values = values();
            for (StatusEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static StatusEnum[] fromJsonArray(JsonArray jsonArray) {
            StatusEnum[] list = new StatusEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public enum AllowedResolversEnum {
        @Json(name = "assigned_provider")
        ASSIGNED_PROVIDER("assigned_provider"),
        @Json(name = "buyer")
        BUYER("buyer"),
        @Json(name = "support")
        SUPPORT("support");

        private String value;

        AllowedResolversEnum(String value) {
            this.value = value;
        }

        public static AllowedResolversEnum fromString(String value) {
            AllowedResolversEnum[] values = values();
            for (AllowedResolversEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static AllowedResolversEnum[] fromJsonArray(JsonArray jsonArray) {
            AllowedResolversEnum[] list = new AllowedResolversEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(ProblemResolution[] array) {
        JsonArray list = new JsonArray();
        for (ProblemResolution item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static ProblemResolution[] fromJsonArray(JsonArray array) {
        ProblemResolution[] list = new ProblemResolution[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ProblemResolution fromJson(JsonObject obj) {
        try {
            return new ProblemResolution(obj);
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
    public static final Parcelable.Creator<ProblemResolution> CREATOR = new Parcelable.Creator<ProblemResolution>() {

        @Override
        public ProblemResolution createFromParcel(Parcel source) {
            try {
                return ProblemResolution.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ProblemResolution[] newArray(int size) {
            return new ProblemResolution[size];
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
