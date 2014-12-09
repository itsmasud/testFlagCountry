package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class UploadSlot {
	@Json(name="maxFileSize")
	private Object _maxFileSize;
	@Json(name = "maxFiles")
	private Integer _maxFiles;
	@Json(name="minFileSize")
	private Object _minFileSize;
	@Json(name = "minFiles")
	private Integer _minFiles;
	@Json(name = "slotId")
	private Integer _slotId;
	@Json(name = "slotName")
	private String _slotName;
	@Json(name="task")
	private Task _task;
	@Json(name = "uploadedDocuments")
	private UploadedDocument[] _uploadedDocuments;

	public UploadSlot() {
	}
	public Object getMaxFileSize(){
		return _maxFileSize;
	}

	public Integer getMaxFiles() {
		return _maxFiles;
	}

	public Object getMinFileSize(){
		return _minFileSize;
	}

	public Integer getMinFiles() {
		return _minFiles;
	}

	public Integer getSlotId() {
		return _slotId;
	}

	public String getSlotName() {
		return _slotName;
	}

	public Task getTask() {
		return _task;
	}

	public UploadedDocument[] getUploadedDocuments() {
		return _uploadedDocuments;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(UploadSlot uploadSlot) {
		try {
			return Serializer.serializeObject(uploadSlot);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static UploadSlot fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(UploadSlot.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
