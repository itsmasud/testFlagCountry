package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Deliverables{
	@Json(name="fileSize")
	private int _fileSize;
	@Json(name="fileType")
	private String _fileType;
	@Json(name="dateUploaded")
	private String _dateUploaded;
	@Json(name="filePath")
	private String _filePath;
	@Json(name="thumbNail")
	private String _thumbNail;
	@Json(name="fileName")
	private String _fileName;

	public Deliverables(){
	}
	public int getFileSize(){
		return _fileSize;
	}

	public String getFileType(){
		return _fileType;
	}

	public String getDateUploaded(){
		return _dateUploaded;
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

	public static JsonObject toJson(Deliverables deliverables) {
		try {
			return Serializer.serializeObject(deliverables);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Deliverables fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Deliverables.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
