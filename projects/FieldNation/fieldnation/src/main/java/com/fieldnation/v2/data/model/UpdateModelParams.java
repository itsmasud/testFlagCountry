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
 * Created by dmgen from swagger on 1/31/17.
 */

public class UpdateModelParams implements Parcelable {
    private static final String TAG = "UpdateModelParams";

    @Json(name = "model")
    private UpdateModelParamsModel _model;

    @Json(name = "updateScheduleByWorkOrder")
    private EventUpdateScheduleByWorkOrder _updateScheduleByWorkOrder;

    public UpdateModelParams() {
    }

    public void setModel(UpdateModelParamsModel model) {
        _model = model;
    }

    public UpdateModelParamsModel getModel() {
        return _model;
    }

    public UpdateModelParams model(UpdateModelParamsModel model) {
        _model = model;
        return this;
    }

    public void setUpdateScheduleByWorkOrder(EventUpdateScheduleByWorkOrder updateScheduleByWorkOrder) {
        _updateScheduleByWorkOrder = updateScheduleByWorkOrder;
    }

    public EventUpdateScheduleByWorkOrder getUpdateScheduleByWorkOrder() {
        return _updateScheduleByWorkOrder;
    }

    public UpdateModelParams updateScheduleByWorkOrder(EventUpdateScheduleByWorkOrder updateScheduleByWorkOrder) {
        _updateScheduleByWorkOrder = updateScheduleByWorkOrder;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UpdateModelParams[] fromJsonArray(JsonArray array) {
        UpdateModelParams[] list = new UpdateModelParams[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UpdateModelParams fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UpdateModelParams.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UpdateModelParams updateModelParams) {
        try {
            return Serializer.serializeObject(updateModelParams);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<UpdateModelParams> CREATOR = new Parcelable.Creator<UpdateModelParams>() {

        @Override
        public UpdateModelParams createFromParcel(Parcel source) {
            try {
                return UpdateModelParams.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UpdateModelParams[] newArray(int size) {
            return new UpdateModelParams[size];
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
