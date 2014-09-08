package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class UploadSlot{
	@Json(name="slotName")
	private String _slotName;
	@Json(name="slotId")
	private Integer _slotId;
	@Json(name="minFiles")
	private Integer _minFiles;
	@Json(name="maxFiles")
	private Integer _maxFiles;
	@Json(name="uploadedDocuments")
	private UploadedDocument[] _uploadedDocuments;

	public UploadSlot(){
	}
	public String getSlotName(){
		return _slotName;
	}

	public Integer getSlotId(){
		return _slotId;
	}

	public Integer getMinFiles(){
		return _minFiles;
	}

	public Integer getMaxFiles(){
		return _maxFiles;
	}

	public UploadedDocument[] getUploadedDocuments(){
		return _uploadedDocuments;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(UploadSlot uploadSlots) {
		try {
			return Serializer.serializeObject(uploadSlots);
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
