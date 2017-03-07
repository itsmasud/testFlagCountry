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

public class MessageProblemType implements Parcelable {
    private static final String TAG = "MessageProblemType";

    @Json(name = "description")
    private String _description;

    @Json(name = "id")
    private Integer _id;

    @Source
    private JsonObject SOURCE;

    public MessageProblemType() {
        SOURCE = new JsonObject();
    }

    public MessageProblemType(JsonObject obj) {
        SOURCE = obj;
    }

    public void setDescription(String description) throws ParseException {
        _description = description;
        SOURCE.put("description", description);
    }

    public String getDescription() {
        try {
            if (_description != null)
                return _description;

            if (SOURCE.has("description") && SOURCE.get("description") != null)
                _description = SOURCE.getString("description");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _description;
    }

    public MessageProblemType description(String description) throws ParseException {
        _description = description;
        SOURCE.put("description", description);
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        try {
            if (_id != null)
                return _id;

            if (SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public MessageProblemType id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(MessageProblemType[] array) {
        JsonArray list = new JsonArray();
        for (MessageProblemType item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static MessageProblemType[] fromJsonArray(JsonArray array) {
        MessageProblemType[] list = new MessageProblemType[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static MessageProblemType fromJson(JsonObject obj) {
        try {
            return new MessageProblemType(obj);
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
    public static final Parcelable.Creator<MessageProblemType> CREATOR = new Parcelable.Creator<MessageProblemType>() {

        @Override
        public MessageProblemType createFromParcel(Parcel source) {
            try {
                return MessageProblemType.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public MessageProblemType[] newArray(int size) {
            return new MessageProblemType[size];
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
