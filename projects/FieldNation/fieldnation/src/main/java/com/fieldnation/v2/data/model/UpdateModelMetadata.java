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

public class UpdateModelMetadata implements Parcelable {
    private static final String TAG = "UpdateModelMetadata";

    @Json(name = "data")
    private UpdateModelMetadataData _data;

    @Json(name = "universal_context")
    private UpdateModelMetadataUniversalContext _universalContext;

    @Source
    private JsonObject SOURCE;

    public UpdateModelMetadata() {
        SOURCE = new JsonObject();
    }

    public UpdateModelMetadata(JsonObject obj) {
        SOURCE = obj;
    }

    public void setData(UpdateModelMetadataData data) throws ParseException {
        _data = data;
        SOURCE.put("data", data.getJson());
    }

    public UpdateModelMetadataData getData() {
        try {
            if (_data == null && SOURCE.has("data") && SOURCE.get("data") != null)
                _data = UpdateModelMetadataData.fromJson(SOURCE.getJsonObject("data"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_data == null)
            _data = new UpdateModelMetadataData();

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
        try {
            if (_universalContext == null && SOURCE.has("universal_context") && SOURCE.get("universal_context") != null)
                _universalContext = UpdateModelMetadataUniversalContext.fromJson(SOURCE.getJsonObject("universal_context"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_universalContext == null)
            _universalContext = new UpdateModelMetadataUniversalContext();

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
            return new UpdateModelMetadata(obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

}
