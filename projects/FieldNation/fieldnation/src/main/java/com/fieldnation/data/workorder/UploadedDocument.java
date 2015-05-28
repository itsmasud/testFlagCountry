package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class UploadedDocument implements Parcelable {
    @Json(name = "downloadLink")
    private String _downloadLink;
    @Json(name = "fileName")
    private String _fileName;
    @Json(name = "fileSize")
    private Integer _fileSize;
    @Json(name = "fileType")
    private String _fileType;
    //	@Json(name="note")
    //	private String _note;
    //    @Json(name = "reviewComment")
    //    private String _reviewComment;
    //    @Json(name = "reviewUserId")
    //    private Integer _reviewUserId;
    //	@Json(name = "reviewUserName")
    //	private String _reviewUserName;
    //    @Json(name = "status")
    //    private Integer _status;
    //    @Json(name = "storageSrc")
    //    private String _storageSrc;
    //    @Json(name = "storageType")
    //    private String _storageType;
    //	@Json(name="thumbnailFilename")
    //	private String _thumbnailFilename;
    @Json(name = "uploadedTime")
    private String _uploadedTime;
    @Json(name = "uploaderUserId")
    private Integer _uploaderUserId;
    @Json(name = "uploaderUserName")
    private String _uploaderUserName;
    //    @Json(name = "workorderId")
    //    private Integer _workorderId;
    @Json(name = "workorderUploadId")
    private Integer _id;
    //    @Json(name = "workorderUploadSlotId")
    //    private Integer _workorderUploadSlotId;

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

    //    public String getReviewComment() {
    //        return _reviewComment;
    //    }
    //
    //    public Integer getReviewUserId() {
    //        return _reviewUserId;
    //    }
    //
    //    public String getReviewUserName() {
    //        return _reviewUserName;
    //    }
    //
    //    public Integer getStatus() {
    //        return _status;
    //    }
    //
    //    public String getStorageSrc() {
    //        return _storageSrc;
    //    }
    //
    //    public String getStorageType() {
    //        return _storageType;
    //    }
    //
    //    public String getThumbnailFilename() {
    //        return _thumbnailFilename;
    //    }

    public String getUploadedTime() {
        return _uploadedTime;
    }

    public Integer getUploaderUserId() {
        return _uploaderUserId;
    }

    public String getUploaderUserName() {
        return _uploaderUserName;
    }

    //    public Integer getWorkorderId() {
    //        return _workorderId;
    //    }

    public Integer getId() {
        return _id;
    }

    //    public Integer getWorkorderUploadSlotId() {
    //        return _workorderUploadSlotId;
    //    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UploadedDocument uploadedDocument) {
        try {
            return Serializer.serializeObject(uploadedDocument);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static UploadedDocument fromJson(JsonObject json) {
        try {
            return Serializer.unserializeObject(UploadedDocument.class, json);
        } catch (Exception ex) {
            ex.printStackTrace();
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
                ex.printStackTrace();
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
