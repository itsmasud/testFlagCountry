package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fntools.ISO8601;

import java.text.ParseException;
import java.util.Comparator;

public class UploadedDocument implements Parcelable {
    private static final String TAG = "UploadedDocument";

    @Json(name = "downloadLink")
    private String _downloadLink;
    @Json(name = "downloadThumbLink")
    private String _downloadThumbLink;
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

    public String getDownloadThumbLink() {
        return _downloadThumbLink;
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
            return Unserializer.unserializeObject(UploadedDocument.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }


    public static class DateTimeComparator implements Comparator<UploadedDocument> {
        @Override
        public int compare(UploadedDocument lhs, UploadedDocument rhs) {
            try {
                long l = ISO8601.toUtc(lhs.getUploadedTime());
                long r = ISO8601.toUtc(rhs.getUploadedTime());

                if (l > r)
                    return -1;
                else if (l < r)
                    return 1;
                else
                    return 0;
            } catch (ParseException e) {
                Log.v(TAG, e);
            }
            return 0;
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
