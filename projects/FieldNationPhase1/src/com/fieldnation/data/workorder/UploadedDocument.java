package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class UploadedDocument {
	@Json(name = "uploaderUserName")
	private String _uploaderUserName;
	@Json(name = "fileSize")
	private Integer _fileSize;
	@Json(name = "workorderUploadId")
	private Integer _workorderUploadId;
	@Json(name = "fileName")
	private String _fileName;
	@Json(name = "uploadedTime")
	private String _uploadedTime;
	@Json(name = "status")
	private Integer _status;
	@Json(name = "reviewComment")
	private String _reviewComment;
	@Json(name = "storageType")
	private String _storageType;
	@Json(name = "reviewUserId")
	private Integer _reviewUserId;
	@Json(name = "reviewUserName")
	private String _reviewUserName;
	@Json(name = "thumbnailFilename")
	private String _thumbnailFilename;
	@Json(name = "workorderId")
	private Integer _workorderId;
	@Json(name = "storageSrc")
	private String _storageSrc;
	@Json(name = "downloadLink")
	private String _downloadLink;
	@Json(name = "uploaderUserId")
	private Integer _uploaderUserId;
	@Json(name = "workorderUploadSlotId")
	private Integer _workorderUploadSlotId;
	@Json(name = "fileType")
	private String _fileType;

	public UploadedDocument() {
	}

	public String getUploaderUserName() {
		return _uploaderUserName;
	}

	public Integer getFileSize() {
		return _fileSize;
	}

	public Integer getWorkorderUploadId() {
		return _workorderUploadId;
	}

	public String getFileName() {
		return _fileName;
	}

	public String getUploadedTime() {
		return _uploadedTime;
	}

	public Integer getStatus() {
		return _status;
	}

	public String getReviewComment() {
		return _reviewComment;
	}

	public String getStorageType() {
		return _storageType;
	}

	public Integer getReviewUserId() {
		return _reviewUserId;
	}

	public String getReviewUserName() {
		return _reviewUserName;
	}

	public String getThumbnailFilename() {
		return _thumbnailFilename;
	}

	public Integer getWorkorderId() {
		return _workorderId;
	}

	public String getStorageSrc() {
		return _storageSrc;
	}

	public String getDownloadLink() {
		return _downloadLink;
	}

	public Integer getUploaderUserId() {
		return _uploaderUserId;
	}

	public Integer getWorkorderUploadSlotId() {
		return _workorderUploadSlotId;
	}

	public String getFileType() {
		return _fileType;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(UploadedDocument uploadedDocuments) {
		try {
			return Serializer.serializeObject(uploadedDocuments);
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

}
