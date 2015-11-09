package com.fieldnation.data.workorder;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class UploadingDocument {
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
}
