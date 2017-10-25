package com.fieldnation.analytics.trackers;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;

/**
 * Created by mc on 10/23/17.
 */

public class UUIDGroup implements Parcelable {
    private static final String TAG = "UUIDGroup";

    @Json
    public String uuid = "";
    @Json
    public String parentUUID = "";

    public UUIDGroup() {
    }

    public UUIDGroup(String parentUUID, String uuid) {
        this.uuid = uuid;
        this.parentUUID = parentUUID;
    }

    private UUIDGroup(Bundle bundle) {
        this.uuid = bundle.getString("uuid");
        this.parentUUID = bundle.getString("parentUUID");
    }

    public JsonObject toJson() {
        try {
            return Serializer.serializeObject(this);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public static UUIDGroup fromJson(JsonObject object) {
        try {
            return Unserializer.unserializeObject(UUIDGroup.class, object);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    @Override
    public String toString() {
        String str = null;

        if (!misc.isEmptyOrNull(parentUUID) && parentUUID.length() > 12) {
            str = "P:" + parentUUID.substring(parentUUID.length() - 12);
        }

        if (!misc.isEmptyOrNull(uuid) && uuid.length() > 12) {
            if (str == null) {
                str = "U:" + uuid.substring(uuid.length() - 12);
            } else {
                str = str + " U:" + uuid.substring(uuid.length() - 12);
            }
        }

        return str;
    }

    public static final Parcelable.Creator<UUIDGroup> CREATOR = new Parcelable.Creator<UUIDGroup>() {
        @Override
        public UUIDGroup createFromParcel(Parcel source) {
            try {
                Bundle bundle = source.readBundle(getClass().getClassLoader());
                return new UUIDGroup(bundle);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            return null;
        }

        @Override
        public UUIDGroup[] newArray(int size) {
            return new UUIDGroup[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uuid);
        bundle.putString("parentUUID", parentUUID);
        dest.writeBundle(bundle);
    }
}
