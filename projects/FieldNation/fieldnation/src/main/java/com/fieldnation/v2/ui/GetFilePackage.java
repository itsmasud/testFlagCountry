package com.fieldnation.v2.ui;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnlog.Log;

public class GetFilePackage implements Parcelable {
    private static final String TAG = "GetFilePackage";
    public String postfix;
    public Intent intent;
    public ResolveInfo resolveInfo;
    public String appName;
    public Drawable icon;


    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(GetFilePackage pack) {
        try {
            return Serializer.serializeObject(pack);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static GetFilePackage fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(GetFilePackage.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }


    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Creator<GetFilePackage> CREATOR = new Creator<GetFilePackage>() {
        @Override
        public GetFilePackage createFromParcel(Parcel source) {
            try {
                return GetFilePackage.fromJson((JsonObject) (source.readParcelable(JsonObject.class.getClassLoader())));
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            return null;
        }

        @Override
        public GetFilePackage[] newArray(int size) {
            return new GetFilePackage[size];
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