package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/30/17.
 */

public class MessageProblem implements Parcelable {
    private static final String TAG = "MessageProblem";

    @Json(name = "flag_id")
    private Integer _flagId;

    @Json(name = "type")
    private MessageProblemType _type;

    @Json(name = "resolved")
    private Boolean _resolved;

    public MessageProblem() {
    }

    public void setFlagId(Integer flagId) {
        _flagId = flagId;
    }

    public Integer getFlagId() {
        return _flagId;
    }

    public MessageProblem flagId(Integer flagId) {
        _flagId = flagId;
        return this;
    }

    public void setType(MessageProblemType type) {
        _type = type;
    }

    public MessageProblemType getType() {
        return _type;
    }

    public MessageProblem type(MessageProblemType type) {
        _type = type;
        return this;
    }

    public void setResolved(Boolean resolved) {
        _resolved = resolved;
    }

    public Boolean getResolved() {
        return _resolved;
    }

    public MessageProblem resolved(Boolean resolved) {
        _resolved = resolved;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static MessageProblem fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(MessageProblem.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(MessageProblem messageProblem) {
        try {
            return Serializer.serializeObject(messageProblem);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
