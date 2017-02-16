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

public class MessageProblem implements Parcelable {
    private static final String TAG = "MessageProblem";

    @Json(name = "flag_id")
    private Integer _flagId;

    @Json(name = "resolved")
    private Boolean _resolved;

    @Json(name = "type")
    private MessageProblemType _type;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public MessageProblem() {
    }

    public void setFlagId(Integer flagId) throws ParseException {
        _flagId = flagId;
        SOURCE.put("flag_id", flagId);
    }

    public Integer getFlagId() {
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
        return _type;
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
            return Unserializer.unserializeObject(MessageProblem.class, obj);
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
}
