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
 * Created by dmgen from swagger.
 */

public class Status implements Parcelable {
    private static final String TAG = "Status";

    @Json(name = "code")
    private String _code;

    @Json(name = "delay")
    private Integer _delay;

    @Json(name = "is_routed")
    private Boolean _isRouted;

    @Json(name = "sub_status")
    private String _subStatus;

    @Json(name = "display")
    private String _display;

    @Json(name = "name")
    private String _name;

    @Json(name = "problem_reported")
    private Boolean _problemReported;

    @Json(name = "ncns")
    private Boolean _ncns;

    @Json(name = "correlation_id")
    private String _correlationId;

    @Json(name = "id")
    private Integer _id;

    public Status() {
    }

    public void setCode(String code) {
        _code = code;
    }

    public String getCode() {
        return _code;
    }

    public Status code(String code) {
        _code = code;
        return this;
    }

    public void setDelay(Integer delay) {
        _delay = delay;
    }

    public Integer getDelay() {
        return _delay;
    }

    public Status delay(Integer delay) {
        _delay = delay;
        return this;
    }

    public void setIsRouted(Boolean isRouted) {
        _isRouted = isRouted;
    }

    public Boolean getIsRouted() {
        return _isRouted;
    }

    public Status isRouted(Boolean isRouted) {
        _isRouted = isRouted;
        return this;
    }

    public void setSubStatus(String subStatus) {
        _subStatus = subStatus;
    }

    public String getSubStatus() {
        return _subStatus;
    }

    public Status subStatus(String subStatus) {
        _subStatus = subStatus;
        return this;
    }

    public void setDisplay(String display) {
        _display = display;
    }

    public String getDisplay() {
        return _display;
    }

    public Status display(String display) {
        _display = display;
        return this;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public Status name(String name) {
        _name = name;
        return this;
    }

    public void setProblemReported(Boolean problemReported) {
        _problemReported = problemReported;
    }

    public Boolean getProblemReported() {
        return _problemReported;
    }

    public Status problemReported(Boolean problemReported) {
        _problemReported = problemReported;
        return this;
    }

    public void setNcns(Boolean ncns) {
        _ncns = ncns;
    }

    public Boolean getNcns() {
        return _ncns;
    }

    public Status ncns(Boolean ncns) {
        _ncns = ncns;
        return this;
    }

    public void setCorrelationId(String correlationId) {
        _correlationId = correlationId;
    }

    public String getCorrelationId() {
        return _correlationId;
    }

    public Status correlationId(String correlationId) {
        _correlationId = correlationId;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public Status id(Integer id) {
        _id = id;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Status[] fromJsonArray(JsonArray array) {
        Status[] list = new Status[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Status fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Status.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Status status) {
        try {
            return Serializer.serializeObject(status);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Status> CREATOR = new Parcelable.Creator<Status>() {

        @Override
        public Status createFromParcel(Parcel source) {
            try {
                return Status.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Status[] newArray(int size) {
            return new Status[size];
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
