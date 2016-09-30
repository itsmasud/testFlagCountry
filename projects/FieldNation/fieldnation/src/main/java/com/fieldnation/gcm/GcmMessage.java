package com.fieldnation.gcm;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.data.v2.actions.Action;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by Michael on 9/29/2016.
 */

public class GcmMessage implements Parcelable {
    public static final String TAG = "GcmMessage";

    public Action[] actions;
    @Json
    public GcmAlert alert;
    @Json
    public String sound;
    @Json(name = "custom_data")
    public String customData;
    @Json
    public String category;

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(GcmMessage gcmMessage) {
        try {
            JsonObject obj = Serializer.serializeObject(gcmMessage);

            if (gcmMessage.actions != null && gcmMessage.actions.length > 0) {
                JsonArray ja = Action.toJsonArray(gcmMessage.actions);
                obj.put("actions", ja);
            }

            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static GcmMessage fromJson(JsonObject json) {
        try {
            GcmMessage obj = Unserializer.unserializeObject(GcmMessage.class, json);

            if (json.has("actions"))
                obj.actions = Action.parseActions(json.getJsonArray("actions"));

            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<GcmMessage> CREATOR = new Parcelable.Creator<GcmMessage>() {

        @Override
        public GcmMessage createFromParcel(Parcel source) {
            try {
                return GcmMessage.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public GcmMessage[] newArray(int size) {
            return new GcmMessage[size];
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
