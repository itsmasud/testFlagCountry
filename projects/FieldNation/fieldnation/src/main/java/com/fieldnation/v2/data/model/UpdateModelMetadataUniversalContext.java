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

public class UpdateModelMetadataUniversalContext implements Parcelable {
    private static final String TAG = "UpdateModelMetadataUniversalContext";

    @Json(name = "correlation_id")
    private String _correlationId;

    @Source
    private JsonObject SOURCE;

    public UpdateModelMetadataUniversalContext() {
        SOURCE = new JsonObject();
    }

    public UpdateModelMetadataUniversalContext(JsonObject obj) {
        SOURCE = obj;
    }

    public void setCorrelationId(String correlationId) throws ParseException {
        _correlationId = correlationId;
        SOURCE.put("correlation_id", correlationId);
    }

    public String getCorrelationId() {
        try {
            if (_correlationId == null && SOURCE.has("correlation_id") && SOURCE.get("correlation_id") != null)
                _correlationId = SOURCE.getString("correlation_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _correlationId;
    }

    public UpdateModelMetadataUniversalContext correlationId(String correlationId) throws ParseException {
        _correlationId = correlationId;
        SOURCE.put("correlation_id", correlationId);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(UpdateModelMetadataUniversalContext[] array) {
        JsonArray list = new JsonArray();
        for (UpdateModelMetadataUniversalContext item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static UpdateModelMetadataUniversalContext[] fromJsonArray(JsonArray array) {
        UpdateModelMetadataUniversalContext[] list = new UpdateModelMetadataUniversalContext[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UpdateModelMetadataUniversalContext fromJson(JsonObject obj) {
        try {
            return new UpdateModelMetadataUniversalContext(obj);
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
    public static final Parcelable.Creator<UpdateModelMetadataUniversalContext> CREATOR = new Parcelable.Creator<UpdateModelMetadataUniversalContext>() {

        @Override
        public UpdateModelMetadataUniversalContext createFromParcel(Parcel source) {
            try {
                return UpdateModelMetadataUniversalContext.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UpdateModelMetadataUniversalContext[] newArray(int size) {
            return new UpdateModelMetadataUniversalContext[size];
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
        return !misc.isEmptyOrNull(getCorrelationId());
    }
}
