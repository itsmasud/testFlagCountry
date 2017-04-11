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

    /*
1    Created
2    Published
3    Assigned
4    Work Done
5    Approved
6    Paid
7    Cancelled
8    Postponed
9    Routed
10    Deleted
     */

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
    private JsonObject SOURCE;

    public Status() {
        SOURCE = new JsonObject();
    }

    public Status(JsonObject obj) {
        SOURCE = obj;
    }

    public void setCode(String code) throws ParseException {
        _code = code;
        SOURCE.put("code", code);
    }

    public String getCode() {
        try {
            if (_code == null && SOURCE.has("code") && SOURCE.get("code") != null)
                _code = SOURCE.getString("code");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_correlationId == null && SOURCE.has("correlation_id") && SOURCE.get("correlation_id") != null)
                _correlationId = SOURCE.getString("correlation_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_delay == null && SOURCE.has("delay") && SOURCE.get("delay") != null)
                _delay = SOURCE.getInt("delay");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_display == null && SOURCE.has("display") && SOURCE.get("display") != null)
                _display = SOURCE.getString("display");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_isRouted == null && SOURCE.has("is_routed") && SOURCE.get("is_routed") != null)
                _isRouted = SOURCE.getBoolean("is_routed");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_name == null && SOURCE.has("name") && SOURCE.get("name") != null)
                _name = SOURCE.getString("name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_ncns == null && SOURCE.has("ncns") && SOURCE.get("ncns") != null)
                _ncns = SOURCE.getBoolean("ncns");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_problemReported == null && SOURCE.has("problem_reported") && SOURCE.get("problem_reported") != null)
                _problemReported = SOURCE.getBoolean("problem_reported");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_subStatus == null && SOURCE.has("sub_status") && SOURCE.get("sub_status") != null)
                _subStatus = SOURCE.getString("sub_status");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
            return new Status(obj);
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

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return true;
    }
}
