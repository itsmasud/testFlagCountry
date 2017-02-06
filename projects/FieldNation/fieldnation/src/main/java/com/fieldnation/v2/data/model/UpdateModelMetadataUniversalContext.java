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
 * Created by dmgen from swagger.
 */

public class UpdateModelMetadataUniversalContext implements Parcelable {
    private static final String TAG = "UpdateModelMetadataUniversalContext";

    @Json(name = "correlation_id")
    private String _correlationId;

    public UpdateModelMetadataUniversalContext() {
    }

    public void setCorrelationId(String correlationId) {
        _correlationId = correlationId;
    }

    public String getCorrelationId() {
        return _correlationId;
    }

    public UpdateModelMetadataUniversalContext correlationId(String correlationId) {
        _correlationId = correlationId;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UpdateModelMetadataUniversalContext[] fromJsonArray(JsonArray array) {
        UpdateModelMetadataUniversalContext[] list = new UpdateModelMetadataUniversalContext[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UpdateModelMetadataUniversalContext fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UpdateModelMetadataUniversalContext.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UpdateModelMetadataUniversalContext updateModelMetadataUniversalContext) {
        try {
            return Serializer.serializeObject(updateModelMetadataUniversalContext);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
