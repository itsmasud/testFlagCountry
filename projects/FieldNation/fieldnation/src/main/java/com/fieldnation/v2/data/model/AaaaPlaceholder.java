package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class AaaaPlaceholder implements Parcelable {
    private static final String TAG = "AaaaPlaceholder";

    public AaaaPlaceholder() {
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static AaaaPlaceholder[] fromJsonArray(JsonArray array) {
        AaaaPlaceholder[] list = new AaaaPlaceholder[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static AaaaPlaceholder fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(AaaaPlaceholder.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(AaaaPlaceholder aaaaPlaceholder) {
        try {
            return Serializer.serializeObject(aaaaPlaceholder);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<AaaaPlaceholder> CREATOR = new Parcelable.Creator<AaaaPlaceholder>() {

        @Override
        public AaaaPlaceholder createFromParcel(Parcel source) {
            try {
                return AaaaPlaceholder.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public AaaaPlaceholder[] newArray(int size) {
            return new AaaaPlaceholder[size];
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
