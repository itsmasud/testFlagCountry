package com.fieldnation.data.workorder;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;

public class Deliverable {
    private static final String TAG = "Deliverable";

    @Json(name = "dateUploaded")
    private String _dateUploaded;
    @Json(name = "fileName")
    private String _fileName;
    @Json(name = "filePath")
    private String _filePath;
    @Json(name = "fileSize")
    private Integer _fileSize;
    @Json(name = "fileType")
    private String _fileType;
    @Json(name = "reviewComment")
    private String _reviewComment;
    @Json(name = "reviewTime")
    private Integer _reviewTime;
    @Json(name = "reviewUserId")
    private Integer _reviewUserId;
    @Json(name = "status")
    private Integer _status;
    @Json(name = "storageSrc")
    private String _storageSrc;
    @Json(name = "storageType")
    private String _storageType;
    @Json(name = "thumbNail")
    private String _thumbNail;
    @Json(name = "thumbnailFilename")
    private String _thumbnailFilename;
    @Json(name = "thumbnailFilesize")
    private Integer _thumbnailFilesize;
    @Json(name = "uploadUniqueId")
    private String _uploadUniqueId;
    @Json(name = "uploadedBy")
    private User _uploadedBy;
    @Json(name = "uploadedTime")
    private String _uploadedTime;
    @Json(name = "uploaderUserId")
    private Integer _uploaderUserId;
    @Json(name = "userId")
    private Integer _userId;
    @Json(name = "workorderId")
    private Integer _workorderId;
    @Json(name = "workorderUploadId")
    private Integer _workorderUploadId;
    @Json(name = "workorderUploadSlotId")
    private Integer _workorderUploadSlotId;

    public Deliverable() {
    }

    public String getDateUploaded() {
        return _dateUploaded;
    }

    public String getFileName() {
        return _fileName;
    }

    public String getFilePath() {
        return _filePath;
    }

    public Integer getFileSize() {
        return _fileSize;
    }

    public String getFileType() {
        return _fileType;
    }

    public String getReviewComment() {
        return _reviewComment;
    }

    public Integer getReviewTime() {
        return _reviewTime;
    }

    public Integer getReviewUserId() {
        return _reviewUserId;
    }

    public Integer getStatus() {
        return _status;
    }

    public String getStorageSrc() {
        return _storageSrc;
    }

    public String getStorageType() {
        return _storageType;
    }

    public String getThumbNail() {
        return _thumbNail;
    }

    public String getThumbnailFilename() {
        return _thumbnailFilename;
    }

    public Integer getThumbnailFilesize() {
        return _thumbnailFilesize;
    }

    public String getUploadUniqueId() {
        return _uploadUniqueId;
    }

    public User getUploadedBy() {
        return _uploadedBy;
    }

    public String getUploadedTime() {
        return _uploadedTime;
    }

    public Integer getUploaderUserId() {
        return _uploaderUserId;
    }

    public Integer getUserId() {
        return _userId;
    }

    public Integer getWorkorderId() {
        return _workorderId;
    }

    public Integer getWorkorderUploadId() {
        return _workorderUploadId;
    }

    public Integer getWorkorderUploadSlotId() {
        return _workorderUploadSlotId;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Deliverable deliverable) {
        try {
            return Serializer.serializeObject(deliverable);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Deliverable fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Deliverable.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

}
