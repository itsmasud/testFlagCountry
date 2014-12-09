package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class SlotData{
	@Json(name="maxFileSize")
	private Object _maxFileSize;
	@Json(name="maxFiles")
	private Integer _maxFiles;
	@Json(name="minFileSize")
	private Object _minFileSize;
	@Json(name="minFiles")
	private Integer _minFiles;
	@Json(name="slotId")
	private Integer _slotId;
	@Json(name="slotName")
	private String _slotName;

	public SlotData(){
	}
	public Object getMaxFileSize(){
		return _maxFileSize;
	}

	public Integer getMaxFiles(){
		return _maxFiles;
	}

	public Object getMinFileSize(){
		return _minFileSize;
	}

	public Integer getMinFiles(){
		return _minFiles;
	}

	public Integer getSlotId(){
		return _slotId;
	}

	public String getSlotName(){
		return _slotName;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(SlotData slotData) {
		try {
			return Serializer.serializeObject(slotData);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static SlotData fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(SlotData.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
