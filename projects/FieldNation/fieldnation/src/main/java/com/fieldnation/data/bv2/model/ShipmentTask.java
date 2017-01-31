package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class ShipmentTask implements Parcelable {
    private static final String TAG = "ShipmentTask";

    @Json(name = "id")
    private Double _id;

    public ShipmentTask() {
    }

    public void setId(Double id) {
        _id = id;
    }

    public Double getId() {
        return _id;
    }

    public ShipmentTask id(Double id) {
        _id = id;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ShipmentTask[] fromJsonArray(JsonArray array) {
        ShipmentTask[] list = new ShipmentTask[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ShipmentTask fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ShipmentTask.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ShipmentTask shipmentTask) {
        try {
            return Serializer.serializeObject(shipmentTask);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<ShipmentTask> CREATOR = new Parcelable.Creator<ShipmentTask>() {

        @Override
        public ShipmentTask createFromParcel(Parcel source) {
            try {
                return ShipmentTask.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ShipmentTask[] newArray(int size) {
            return new ShipmentTask[size];
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
