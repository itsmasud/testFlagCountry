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

public class Status implements Parcelable {
    private static final String TAG = "Status";

    @Json(name = "code")
    private String _code;

    @Json(name = "correlation_id")
    private String _correlationId;

    @Json(name = "delay")
    private Integer _delay;

    @Json(name = "display")
    private String _display;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "is_routed")
    private Boolean _isRouted;

    @Json(name = "name")
    private String _name;

    @Json(name = "ncns")
    private Boolean _ncns;

    @Json(name = "problem_reported")
    private Boolean _problemReported;

    @Json(name = "sub_status")
    private String _subStatus;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public Status() {
    }

    public void setCode(String code) throws ParseException {
        _code = code;
        SOURCE.put("code", code);
    }

    public String getCode() {
        return _code;
    }

    public Status code(String code) throws ParseException {
        _code = code;
        SOURCE.put("code", code);
        return this;
    }

    public void setCorrelationId(String correlationId) throws ParseException {
        _correlationId = correlationId;
        SOURCE.put("correlation_id", correlationId);
    }

    public String getCorrelationId() {
        return _correlationId;
    }

    public Status correlationId(String correlationId) throws ParseException {
        _correlationId = correlationId;
        SOURCE.put("correlation_id", correlationId);
        return this;
    }

    public void setDelay(Integer delay) throws ParseException {
        _delay = delay;
        SOURCE.put("delay", delay);
    }

    public Integer getDelay() {
        return _delay;
    }

    public Status delay(Integer delay) throws ParseException {
        _delay = delay;
        SOURCE.put("delay", delay);
        return this;
    }

    public void setDisplay(String display) throws ParseException {
        _display = display;
        SOURCE.put("display", display);
    }

    public String getDisplay() {
        return _display;
    }

    public Status display(String display) throws ParseException {
        _display = display;
        SOURCE.put("display", display);
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        return _id;
    }

    public Status id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setIsRouted(Boolean isRouted) throws ParseException {
        _isRouted = isRouted;
        SOURCE.put("is_routed", isRouted);
    }

    public Boolean getIsRouted() {
        return _isRouted;
    }

    public Status isRouted(Boolean isRouted) throws ParseException {
        _isRouted = isRouted;
        SOURCE.put("is_routed", isRouted);
        return this;
    }

    public void setName(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
    }

    public String getName() {
        return _name;
    }

    public Status name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    public void setNcns(Boolean ncns) throws ParseException {
        _ncns = ncns;
        SOURCE.put("ncns", ncns);
    }

    public Boolean getNcns() {
        return _ncns;
    }

    public Status ncns(Boolean ncns) throws ParseException {
        _ncns = ncns;
        SOURCE.put("ncns", ncns);
        return this;
    }

    public void setProblemReported(Boolean problemReported) throws ParseException {
        _problemReported = problemReported;
        SOURCE.put("problem_reported", problemReported);
    }

    public Boolean getProblemReported() {
        return _problemReported;
    }

    public Status problemReported(Boolean problemReported) throws ParseException {
        _problemReported = problemReported;
        SOURCE.put("problem_reported", problemReported);
        return this;
    }

    public void setSubStatus(String subStatus) throws ParseException {
        _subStatus = subStatus;
        SOURCE.put("sub_status", subStatus);
    }

    public String getSubStatus() {
        return _subStatus;
    }

    public Status subStatus(String subStatus) throws ParseException {
        _subStatus = subStatus;
        SOURCE.put("sub_status", subStatus);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Status[] array) {
        JsonArray list = new JsonArray();
        for (Status item : array) {
            list.add(item.getJson());
        }
        return list;
    }

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

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}
