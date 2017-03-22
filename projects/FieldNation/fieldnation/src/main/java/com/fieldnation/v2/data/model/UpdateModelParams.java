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

public class UpdateModelParams implements Parcelable {
    private static final String TAG = "UpdateModelParams";

    @Json(name = "model")
    private UpdateModelParamsModel _model;

    @Json(name = "updateScheduleByWorkOrder")
    private EventUpdateScheduleByWorkOrder _updateScheduleByWorkOrder;

    @Source
    private JsonObject SOURCE;

    public UpdateModelParams() {
        SOURCE = new JsonObject();
    }

    public UpdateModelParams(JsonObject obj) {
        SOURCE = obj;
    }

    public void setModel(UpdateModelParamsModel model) throws ParseException {
        _model = model;
        SOURCE.put("model", model.getJson());
    }

    public UpdateModelParamsModel getModel() {
        try {
            if (_model == null && SOURCE.has("model") && SOURCE.get("model") != null)
                _model = UpdateModelParamsModel.fromJson(SOURCE.getJsonObject("model"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_model != null && _model.isSet())
        return _model;

        return null;
    }

    public UpdateModelParams model(UpdateModelParamsModel model) throws ParseException {
        _model = model;
        SOURCE.put("model", model.getJson());
        return this;
    }

    public void setUpdateScheduleByWorkOrder(EventUpdateScheduleByWorkOrder updateScheduleByWorkOrder) throws ParseException {
        _updateScheduleByWorkOrder = updateScheduleByWorkOrder;
        SOURCE.put("updateScheduleByWorkOrder", updateScheduleByWorkOrder.getJson());
    }

    public EventUpdateScheduleByWorkOrder getUpdateScheduleByWorkOrder() {
        try {
            if (_updateScheduleByWorkOrder == null && SOURCE.has("updateScheduleByWorkOrder") && SOURCE.get("updateScheduleByWorkOrder") != null)
                _updateScheduleByWorkOrder = EventUpdateScheduleByWorkOrder.fromJson(SOURCE.getJsonObject("updateScheduleByWorkOrder"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_updateScheduleByWorkOrder != null && _updateScheduleByWorkOrder.isSet())
        return _updateScheduleByWorkOrder;

        return null;
    }

    public UpdateModelParams updateScheduleByWorkOrder(EventUpdateScheduleByWorkOrder updateScheduleByWorkOrder) throws ParseException {
        _updateScheduleByWorkOrder = updateScheduleByWorkOrder;
        SOURCE.put("updateScheduleByWorkOrder", updateScheduleByWorkOrder.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(UpdateModelParams[] array) {
        JsonArray list = new JsonArray();
        for (UpdateModelParams item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static UpdateModelParams[] fromJsonArray(JsonArray array) {
        UpdateModelParams[] list = new UpdateModelParams[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UpdateModelParams fromJson(JsonObject obj) {
        try {
            return new UpdateModelParams(obj);
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
        dest.writeParcelable(getJson(), flags);
    }

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return true;
    }
}
