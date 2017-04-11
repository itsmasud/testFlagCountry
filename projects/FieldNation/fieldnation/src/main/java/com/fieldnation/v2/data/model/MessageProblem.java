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

public class MessageProblem implements Parcelable {
    private static final String TAG = "MessageProblem";

    @Json(name = "can_resolve")
    private Boolean _canResolve;

    @Json(name = "escalate_to_performance")
    private Boolean _escalateToPerformance;

    @Json(name = "flag_id")
    private Integer _flagId;

    @Json(name = "resolved")
    private Boolean _resolved;

    @Json(name = "type")
    private MessageProblemType _type;

    @Source
    private JsonObject SOURCE;

    public MessageProblem() {
        SOURCE = new JsonObject();
    }

    public MessageProblem(JsonObject obj) {
        SOURCE = obj;
    }

    public void setCanResolve(Boolean canResolve) throws ParseException {
        _canResolve = canResolve;
        SOURCE.put("can_resolve", canResolve);
    }

    public Boolean getCanResolve() {
        try {
            if (_canResolve == null && SOURCE.has("can_resolve") && SOURCE.get("can_resolve") != null)
                _canResolve = SOURCE.getBoolean("can_resolve");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _canResolve;
    }

    public MessageProblem canResolve(Boolean canResolve) throws ParseException {
        _canResolve = canResolve;
        SOURCE.put("can_resolve", canResolve);
        return this;
    }

    public void setEscalateToPerformance(Boolean escalateToPerformance) throws ParseException {
        _escalateToPerformance = escalateToPerformance;
        SOURCE.put("escalate_to_performance", escalateToPerformance);
    }

    public Boolean getEscalateToPerformance() {
        try {
            if (_escalateToPerformance == null && SOURCE.has("escalate_to_performance") && SOURCE.get("escalate_to_performance") != null)
                _escalateToPerformance = SOURCE.getBoolean("escalate_to_performance");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _escalateToPerformance;
    }

    public MessageProblem escalateToPerformance(Boolean escalateToPerformance) throws ParseException {
        _escalateToPerformance = escalateToPerformance;
        SOURCE.put("escalate_to_performance", escalateToPerformance);
        return this;
    }

    public void setFlagId(Integer flagId) throws ParseException {
        _flagId = flagId;
        SOURCE.put("flag_id", flagId);
    }

    public Integer getFlagId() {
        try {
            if (_flagId == null && SOURCE.has("flag_id") && SOURCE.get("flag_id") != null)
                _flagId = SOURCE.getInt("flag_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _flagId;
    }

    public MessageProblem flagId(Integer flagId) throws ParseException {
        _flagId = flagId;
        SOURCE.put("flag_id", flagId);
        return this;
    }

    public void setResolved(Boolean resolved) throws ParseException {
        _resolved = resolved;
        SOURCE.put("resolved", resolved);
    }

    public Boolean getResolved() {
        try {
            if (_resolved == null && SOURCE.has("resolved") && SOURCE.get("resolved") != null)
                _resolved = SOURCE.getBoolean("resolved");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _resolved;
    }

    public MessageProblem resolved(Boolean resolved) throws ParseException {
        _resolved = resolved;
        SOURCE.put("resolved", resolved);
        return this;
    }

    public void setType(MessageProblemType type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.getJson());
    }

    public MessageProblemType getType() {
        try {
            if (_type == null && SOURCE.has("type") && SOURCE.get("type") != null)
                _type = MessageProblemType.fromJson(SOURCE.getJsonObject("type"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_type != null && _type.isSet())
        return _type;

        return null;
    }

    public MessageProblem type(MessageProblemType type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(MessageProblem[] array) {
        JsonArray list = new JsonArray();
        for (MessageProblem item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static MessageProblem[] fromJsonArray(JsonArray array) {
        MessageProblem[] list = new MessageProblem[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static MessageProblem fromJson(JsonObject obj) {
        try {
            return new MessageProblem(obj);
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
    public static final Parcelable.Creator<MessageProblem> CREATOR = new Parcelable.Creator<MessageProblem>() {

        @Override
        public MessageProblem createFromParcel(Parcel source) {
            try {
                return MessageProblem.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public MessageProblem[] newArray(int size) {
            return new MessageProblem[size];
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
