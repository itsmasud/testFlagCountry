package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Document {
	@Json(name="fileName")
	private String _fileName;
	@Json(name="filePath")
	private String _filePath;
	@Json(name = "fileSize")
	private Integer _fileSize;
	@Json(name = "fileType")
	private String _fileType;
	@Json(name="lastUpdated")
	private String _lastUpdated;
	@Json(name="thumbNail")
	private String _thumbNail;
	@Json(name="updatedBy")
	private User _updatedBy;

	public Document() {
	}
	public String getFileName(){
		return _fileName;
	}

	public String getFilePath(){
		return _filePath;
	}

	public Integer getFileSize() {
		return _fileSize;
	}

	public String getFileType() {
		return _fileType;
	}

	public String getLastUpdated(){
		return _lastUpdated;
	}

	public String getThumbNail() {
		return _thumbNail;
	}

	public User getUpdatedBy(){
		return _updatedBy;
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
