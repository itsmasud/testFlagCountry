package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Deliverable {
	@Json(name = "fileSize")
	private Integer _fileSize;
	@Json(name = "fileType")
	private String _fileType;
	@Json(name = "dateUploaded")
	private String _dateUploaded;
	@Json(name = "filePath")
	private String _filePath;
	@Json(name = "thumbNail")
	private String _thumbNail;
	@Json(name = "fileName")
	private String _fileName;

	public Deliverable() {
	}

	public Integer getFileSize() {
		return _fileSize;
	}

	public String getFileType() {
		return _fileType;
	}

	public String getDateUploaded() {
		return _dateUploaded;
	}

	public String getFilePath() {
		return _filePath;
	}

	public String getThumbNail() {
		return _thumbNail;
	}

	public String getFileName() {
		return _fileName;
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
