package com.fieldnation.data.workorder;

import android.net.Uri;
import android.os.*;
import android.os.Bundle;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;

public class UploadingDocument implements Parcelable {
    private static final String TAG = "UploadingDocument";

    private String _fileName;
    private Uri _uri;

    public UploadingDocument() {
    }

    public UploadingDocument(String fileName) {
        _fileName = fileName;
    }

    public UploadingDocument(String fileName, Uri uri) {
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
    public static final Parcelable.Creator<UploadingDocument> CREATOR = new Parcelable.Creator<UploadingDocument>() {

        @Override
        public UploadingDocument createFromParcel(Parcel source) {
            try {
                Bundle bundle = source.readBundle();
                return new UploadingDocument(bundle.getString("fileName"), (Uri) bundle.getParcelable("uri"));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UploadingDocument[] newArray(int size) {
            return new UploadingDocument[size];
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
