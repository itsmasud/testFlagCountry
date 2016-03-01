package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class UploadedDocument implements Parcelable {
    private static final String TAG = "UploadedDocument";

    @Json(name = "downloadLink")
    private String _downloadLink;
    @Json(name = "fileName")
    private String _fileName;
    @Json(name = "fileSize")
    private Integer _fileSize;
    @Json(name = "fileType")
    private String _fileType;
    @Json(name = "uploadedTime")
    private String _uploadedTime;
    @Json(name = "uploaderUserId")
    private Integer _uploaderUserId;
    @Json(name = "uploaderUserName")
    private String _uploaderUserName;
    @Json(name = "workorderUploadId")
    private Integer _id;

    public UploadedDocument() {
    }

    public String getDownloadLink() {
        return _downloadLink;
    }

    public String getFileName() {
        return _fileName;
    }

    public Integer getFileSize() {
        return _fileSize;
    }

    public String getFileType() {
        return _fileType;
    }

    public String getUploadedTime() {
        return _uploadedTime;
    }

    public Integer getUploaderUserId() {
        return _uploaderUserId;
    }

    public String getUploaderUserName() {
        return _uploaderUserName;
    }

    public Integer getId() {
        return _id;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UploadedDocument uploadedDocument) {
        try {
            return Serializer.serializeObject(uploadedDocument);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static UploadedDocument fromJson(JsonObject json) {
        try {
            return Serializer.unserializeObject(UploadedDocument.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<UploadedDocument> CREATOR = new Parcelable.Creator<UploadedDocument>() {

        @Override
        public UploadedDocument createFromParcel(Parcel source) {
            try {
                return UploadedDocument.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UploadedDocument[] newArray(int size) {
            return new UploadedDocument[size];
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
