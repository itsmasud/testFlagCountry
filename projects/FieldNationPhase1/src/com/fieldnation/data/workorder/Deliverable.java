package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Deliverable {
	@Json(name = "workorder_upload_slot_id")
	private Integer _workorderUploadSlotId;
	@Json(name = "workorder_upload_id")
	private Integer _workorderUploadId;
	@Json(name = "workorder_id")
	private Integer _workorderId;
	@Json(name = "user_id")
	private Integer _userId;
	@Json(name = "review_user_id")
	private Integer _reviewUserId;
	@Json(name = "fileType")
	private String _fileType;
	@Json(name = "file_type")
	private String _file_Type;
	@Json(name = "thumbNail")
	private String _thumbNail;
	@Json(name = "upload_unique_id")
	private String _uploadUniqueId;
	@Json(name = "thumbnail_filesize")
	private Integer _thumbnailFilesize;
	@Json(name = "review_time")
	private Integer _reviewTime;
	@Json(name = "fileSize")
	private Integer _fileSize;
	@Json(name = "file_size")
	private Integer _file_Size;
	@Json(name = "filePath")
	private String _filePath;
	@Json(name = "storage_type")
	private String _storageType;
	@Json(name = "review_comment")
	private String _reviewComment;
	@Json(name = "status")
	private Integer _status;
	@Json(name = "thumbnail_filename")
	private String _thumbnailFilename;
	@Json(name = "uploader_user_id")
	private Integer _uploaderUserId;
	@Json(name = "uploaded_time")
	private Integer _uploadedTime;
	@Json(name = "storage_src")
	private String _storageSrc;
	@Json(name = "fileName")
	private String _fileName;
	@Json(name = "dateUploaded")
	private Object _dateUploaded;
	@Json(name = "file_name")
	private String _file_Name;

	public Deliverable() {
	}

	public Integer getWorkorderUploadSlotId() {
		return _workorderUploadSlotId;
	}

	public Integer getWorkorderUploadId() {
		return _workorderUploadId;
	}

	public Integer getWorkorderId() {
		return _workorderId;
	}

	public Integer getUserId() {
		return _userId;
	}

	public Integer getReviewUserId() {
		return _reviewUserId;
	}

	public String getFileType() {
		if (_file_Type != null)
			return _file_Type;
		return _fileType;
	}

	public String getThumbNail() {
		return _thumbNail;
	}

	public String getUploadUniqueId() {
		return _uploadUniqueId;
	}

	public Integer getThumbnailFilesize() {
		return _thumbnailFilesize;
	}

	public Integer getReviewTime() {
		return _reviewTime;
	}

	public Integer getFileSize() {
		if (_file_Size != null)
			return _file_Size;
		return _fileSize;
	}

	public String getFilePath() {
		return _filePath;
	}

	public String getStorageType() {
		return _storageType;
	}

	public String getReviewComment() {
		return _reviewComment;
	}

	public Integer getStatus() {
		return _status;
	}

	public String getThumbnailFilename() {
		return _thumbnailFilename;
	}

	public Integer getUploaderUserId() {
		return _uploaderUserId;
	}

	public Integer getUploadedTime() {
		return _uploadedTime;
	}

	public String getStorageSrc() {
		return _storageSrc;
	}

	public String getFileName() {
		if (_file_Name != null)
			return _file_Name;
		return _fileName;
	}

	public Object getDateUploaded() {
		return _dateUploaded;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(Deliverable deliverable) {
		try {
			return Serializer.serializeObject(deliverable);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Deliverable fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Deliverable.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
