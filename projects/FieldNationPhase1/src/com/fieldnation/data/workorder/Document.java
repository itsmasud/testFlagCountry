package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Document {
	@Json(name = "fileSize")
	private Integer _fileSize;
	@Json(name = "fileType")
	private String _fileType;
	@Json(name="updatedBy")
	private User _updatedBy;
	@Json(name = "filePath")
	private String _filePath;
	@Json(name = "thumbNail")
	private String _thumbNail;
	@Json(name="lastUpdated")
	private String _lastUpdated;
	@Json(name = "fileName")
	private String _fileName;

	public Document() {
	}

	public Integer getFileSize() {
		return _fileSize;
	}

	public String getFileType() {
		return _fileType;
	}

	public User getUpdatedBy(){
		return _updatedBy;
	}

	public String getFilePath() {
		return _filePath;
	}

	public String getThumbNail() {
		return _thumbNail;
	}

	public String getLastUpdated(){
		return _lastUpdated;
	}

	public String getFileName() {
		return _fileName;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(Document document) {
		try {
			return Serializer.serializeObject(document);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Document fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Document.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
