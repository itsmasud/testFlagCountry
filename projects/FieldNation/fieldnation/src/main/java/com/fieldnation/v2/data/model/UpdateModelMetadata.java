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

public class UpdateModelMetadata implements Parcelable {
    private static final String TAG = "UpdateModelMetadata";

    @Json(name = "data")
    private UpdateModelMetadataData _data;

    @Json(name = "universal_context")
    private UpdateModelMetadataUniversalContext _universalContext;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public UpdateModelMetadata() {
    }

    public void setData(UpdateModelMetadataData data) throws ParseException {
        _data = data;
        SOURCE.put("data", data.getJson());
    }

    public UpdateModelMetadataData getData() {
        return _data;
    }

    public UpdateModelMetadata data(UpdateModelMetadataData data) throws ParseException {
        _data = data;
        SOURCE.put("data", data.getJson());
        return this;
    }

    public void setUniversalContext(UpdateModelMetadataUniversalContext universalContext) throws ParseException {
        _universalContext = universalContext;
        SOURCE.put("universal_context", universalContext.getJson());
    }

    public UpdateModelMetadataUniversalContext getUniversalContext() {
        return _universalContext;
    }

    public UpdateModelMetadata universalContext(UpdateModelMetadataUniversalContext universalContext) throws ParseException {
        _universalContext = universalContext;
        SOURCE.put("universal_context", universalContext.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(UpdateModelMetadata[] array) {
        JsonArray list = new JsonArray();
        for (UpdateModelMetadata item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static UpdateModelMetadata[] fromJsonArray(JsonArray array) {
        UpdateModelMetadata[] list = new UpdateModelMetadata[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UpdateModelMetadata fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UpdateModelMetadata.class, obj);
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
    public static final Parcelable.Creator<UpdateModelMetadata> CREATOR = new Parcelable.Creator<UpdateModelMetadata>() {

        @Override
        public UpdateModelMetadata createFromParcel(Parcel source) {
            try {
                return UpdateModelMetadata.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UpdateModelMetadata[] newArray(int size) {
            return new UpdateModelMetadata[size];
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
