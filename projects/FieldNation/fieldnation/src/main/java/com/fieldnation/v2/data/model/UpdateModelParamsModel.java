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

public class UpdateModelParamsModel implements Parcelable {
    private static final String TAG = "UpdateModelParamsModel";

    @Source
    private JsonObject SOURCE = new JsonObject();

    public UpdateModelParamsModel() {
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(UpdateModelParamsModel[] array) {
        JsonArray list = new JsonArray();
        for (UpdateModelParamsModel item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static UpdateModelParamsModel[] fromJsonArray(JsonArray array) {
        UpdateModelParamsModel[] list = new UpdateModelParamsModel[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UpdateModelParamsModel fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UpdateModelParamsModel.class, obj);
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
    public static final Parcelable.Creator<UpdateModelParamsModel> CREATOR = new Parcelable.Creator<UpdateModelParamsModel>() {

        @Override
        public UpdateModelParamsModel createFromParcel(Parcel source) {
            try {
                return UpdateModelParamsModel.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UpdateModelParamsModel[] newArray(int size) {
            return new UpdateModelParamsModel[size];
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
