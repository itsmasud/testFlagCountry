package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Deliverable {
	@Json(name="thumbnailFilename")
	private String _thumbnailFilename;
	@Json(name = "fileType")
	private String _fileType;
	@Json(name="reviewTime")
	private Integer _reviewTime;
	@Json(name="userId")
	private Integer _userId;
	@Json(name="uploadedTime")
	private String _uploadedTime;
	@Json(name="thumbNail")
	private Object _thumbNail;
	@Json(name="workorderId")
	private Integer _workorderId;
	@Json(name="uploadedBy")
	private User _uploadedBy;
	@Json(name="workorderUploadId")
	private Integer _workorderUploadId;
	@Json(name="workorderUploadSlotId")
	private Integer _workorderUploadSlotId;
	@Json(name = "fileSize")
	private Integer _fileSize;
	@Json(name = "filePath")
	private Object _filePath;
	@Json(name="reviewUserId")
	private Integer _reviewUserId;
	@Json(name="thumbnailFilesize")
	private Integer _thumbnailFilesize;
	@Json(name="uploadUniqueId")
	private String _uploadUniqueId;
	@Json(name = "status")
	private Integer _status;
	@Json(name="reviewComment")
	private String _reviewComment;
	@Json(name="storageType")
	private String _storageType;
	@Json(name="uploaderUserId")
	private Integer _uploaderUserId;
	@Json(name="storageSrc")
	private String _storageSrc;
	@Json(name = "fileName")
	private String _fileName;
	@Json(name = "dateUploaded")
	private Object _dateUploaded;

	public Deliverable() {
	}
	public String getThumbnailFilename(){
		return _thumbnailFilename;
	}

	public String getFileType(){
		return _fileType;
	}

	public Integer getReviewTime(){
		return _reviewTime;
	}

	public Integer getUserId() {
		return _userId;
	}

	public String getUploadedTime(){
		return _uploadedTime;
	}

	public Object getThumbNail(){
		return _thumbNail;
	}

	public Integer getWorkorderId(){
		return _workorderId;
	}

	public User getUploadedBy(){
		return _uploadedBy;
	}

	public Integer getWorkorderUploadId(){
		return _workorderUploadId;
	}

	public Integer getWorkorderUploadSlotId(){
		return _workorderUploadSlotId;
	}

	public Integer getFileSize() {
		return _fileSize;
	}

	public Object getFilePath(){
		return _filePath;
	}

	public Integer getReviewUserId(){
		return _reviewUserId;
	}

	public Integer getThumbnailFilesize(){
		return _thumbnailFilesize;
	}

	public String getUploadUniqueId(){
		return _uploadUniqueId;
	}

	public Integer getStatus() {
		return _status;
	}

	public String getReviewComment(){
		return _reviewComment;
	}

	public String getStorageType(){
		return _storageType;
	}

	public Integer getUploaderUserId(){
		return _uploaderUserId;
	}

	public String getStorageSrc() {
		return _storageSrc;
	}

	public String getFileName() {
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
