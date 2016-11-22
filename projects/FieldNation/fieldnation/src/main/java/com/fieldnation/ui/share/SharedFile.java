package com.fieldnation.ui.share;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnlog.Log;

public class SharedFile implements Parcelable {
    private static final String TAG = "SharedFile";

    private String _fileName;
    private Uri _uri;

    public SharedFile() {
    }

    public SharedFile(String fileName) {
        _fileName = fileName;
    }

    public SharedFile(String fileName, Uri uri) {
        _fileName = fileName;
        _uri = uri;
    }


    public String getFileName() {
        return _fileName;
    }

    public Uri getUri() {
        return _uri;
    }


    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<SharedFile> CREATOR = new Parcelable.Creator<SharedFile>() {

        @Override
        public SharedFile createFromParcel(Parcel source) {
            try {
                Bundle bundle = source.readBundle(getClass().getClassLoader());
                return new SharedFile(bundle.getString("fileName"), (Uri) bundle.getParcelable("uri"));
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
        dest.writeBundle(bundle);
    }
}
