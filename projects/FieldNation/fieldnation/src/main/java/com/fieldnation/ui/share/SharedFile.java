package com.fieldnation.ui.share;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.fnlog.Log;

import java.util.UUID;

public class SharedFile implements Parcelable {
    private static final String TAG = "SharedFile";

    private String _fileName;
    private Uri _uri;
    private UUIDGroup _uuid;

    public SharedFile() {
    }

    public SharedFile(String parentUUID, String fileName) {
        _uuid = new UUIDGroup(parentUUID, UUID.randomUUID().toString());
        _fileName = fileName;
    }

    public SharedFile(String parentUUID, String fileName, Uri uri) {
        _uuid = new UUIDGroup(parentUUID, UUID.randomUUID().toString());
        _fileName = fileName;
        _uri = uri;
    }

    private SharedFile(Bundle bundle) {
        _uuid = bundle.getParcelable("uuid");
        _fileName = bundle.getString("fileName");
        _uri = bundle.getParcelable("uri");
    }


    public String getFileName() {
        return _fileName;
    }

    public Uri getUri() {
        return _uri;
    }

    public UUIDGroup getUUID() {
        return _uuid;
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<SharedFile> CREATOR = new Parcelable.Creator<SharedFile>() {

        @Override
        public SharedFile createFromParcel(Parcel source) {
            try {
                Bundle bundle = source.readBundle(getClass().getClassLoader());
                return new SharedFile(bundle);
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public SharedFile[] newArray(int size) {
            return new SharedFile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putString("fileName", _fileName);
        bundle.putParcelable("uri", _uri);
        bundle.putParcelable("uuid", _uuid);
        dest.writeBundle(bundle);
    }
}
