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

public class UpdateModel implements Parcelable {
    private static final String TAG = "UpdateModel";

    @Json(name = "metadata")
    private UpdateModelMetadata _metadata;

    @Json(name = "params")
    private UpdateModelParams _params;

    @Json(name = "service_name")
    private String _serviceName;

    @Json(name = "timestamp")
    private String _timestamp;

    @Json(name = "version")
    private String _version;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public UpdateModel() {
    }

    public void setMetadata(UpdateModelMetadata metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
    }

    public UpdateModelMetadata getMetadata() {
        return _metadata;
    }

    public UpdateModel metadata(UpdateModelMetadata metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
        return this;
    }

    public void setParams(UpdateModelParams params) throws ParseException {
        _params = params;
        SOURCE.put("params", params.getJson());
    }

    public UpdateModelParams getParams() {
        return _params;
    }

    public UpdateModel params(UpdateModelParams params) throws ParseException {
        _params = params;
        SOURCE.put("params", params.getJson());
        return this;
    }

    public void setServiceName(String serviceName) throws ParseException {
        _serviceName = serviceName;
        SOURCE.put("service_name", serviceName);
    }

    public String getServiceName() {
        return _serviceName;
    }

    public UpdateModel serviceName(String serviceName) throws ParseException {
        _serviceName = serviceName;
        SOURCE.put("service_name", serviceName);
        return this;
    }

    public void setTimestamp(String timestamp) throws ParseException {
        _timestamp = timestamp;
        SOURCE.put("timestamp", timestamp);
    }

    public String getTimestamp() {
        return _timestamp;
    }

    public UpdateModel timestamp(String timestamp) throws ParseException {
        _timestamp = timestamp;
        SOURCE.put("timestamp", timestamp);
        return this;
    }

    public void setVersion(String version) throws ParseException {
        _version = version;
        SOURCE.put("version", version);
    }

    public String getVersion() {
        return _version;
    }

    public UpdateModel version(String version) throws ParseException {
        _version = version;
        SOURCE.put("version", version);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(UpdateModel[] array) {
        JsonArray list = new JsonArray();
        for (UpdateModel item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static UpdateModel[] fromJsonArray(JsonArray array) {
        UpdateModel[] list = new UpdateModel[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UpdateModel fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UpdateModel.class, obj);
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
    public static final Parcelable.Creator<UpdateModel> CREATOR = new Parcelable.Creator<UpdateModel>() {

        @Override
        public UpdateModel createFromParcel(Parcel source) {
            try {
                return UpdateModel.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UpdateModel[] newArray(int size) {
            return new UpdateModel[size];
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
