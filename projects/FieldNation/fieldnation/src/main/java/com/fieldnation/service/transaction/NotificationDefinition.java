package com.fieldnation.service.transaction;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.Unserializer;
import com.fieldnation.json.annotations.Json;

/**
 * Created by Michael Carver on 1/7/2016.
 */
public class NotificationDefinition implements Parcelable {
    private static final String TAG = "NotificationDefinition";

    @Json(name = "icon")
    public Integer icon;
    @Json(name = "title")
    public String title;
    @Json(name = "body")
    public String body;
    @Json(name = "ticker")
    public String ticker;

    public NotificationDefinition() {
    }

    public NotificationDefinition(int icon, String title, String ticker, String body) {
        this.icon = icon;
        this.title = title;
        this.ticker = ticker;
        this.body = body;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(NotificationDefinition workorder) {
        try {
            return Serializer.serializeObject(workorder);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static NotificationDefinition fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(NotificationDefinition.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }


    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<NotificationDefinition> CREATOR = new Parcelable.Creator<NotificationDefinition>() {

        @Override
        public NotificationDefinition createFromParcel(Parcel source) {
            try {
                return NotificationDefinition.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public NotificationDefinition[] newArray(int size) {
            return new NotificationDefinition[size];
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
