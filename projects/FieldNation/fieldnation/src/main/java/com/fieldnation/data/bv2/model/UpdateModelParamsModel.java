package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class UpdateModelParamsModel implements Parcelable {
    private static final String TAG = "UpdateModelParamsModel";

    public UpdateModelParamsModel() {
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UpdateModelParamsModel fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UpdateModelParamsModel.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UpdateModelParamsModel updateModelParamsModel) {
        try {
            return Serializer.serializeObject(updateModelParamsModel);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
