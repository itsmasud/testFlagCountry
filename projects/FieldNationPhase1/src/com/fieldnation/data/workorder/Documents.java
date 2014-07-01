package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Documents{
	@Json(name="fileSize")
	private int _fileSize;
	@Json(name="fileType")
	private String _fileType;
	@Json(name="filePath")
	private String _filePath;
	@Json(name="thumbNail")
	private String _thumbNail;
	@Json(name="fileName")
	private String _fileName;

	public Documents(){
	}
	public int getFileSize(){
		return _fileSize;
	}

	public String getFileType(){
		return _fileType;
	}

	public String getFilePath(){
		return _filePath;
	}

	public String getThumbNail(){
		return _thumbNail;
	}

	public String getFileName(){
		return _fileName;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(Documents documents) {
		try {
			return Serializer.serializeObject(documents);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Documents fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Documents.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
