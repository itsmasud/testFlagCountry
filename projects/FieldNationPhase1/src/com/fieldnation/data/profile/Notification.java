package com.fieldnation.data.profile;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Notification{
	@Json(name="viewed")
	private Integer _viewed;
	@Json(name="date")
	private String _date;
	@Json(name="workorderId")
	private Integer _workorderId;
	@Json(name="message")
	private String _message;
	@Json(name="notificationId")
	private Integer _notificationId;
	@Json(name="notificationType")
	private String _notificationType;

	public Notification(){
	}
	public Integer getViewed(){
		return _viewed;
	}

	public String getDate(){
		return _date;
	}

	public Integer getWorkorderId(){
		return _workorderId;
	}

	public String getMessage(){
		return _message;
	}

	public Integer getNotificationId(){
		return _notificationId;
	}

	public String getNotificationType(){
		return _notificationType;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(Notification notification) {
		try {
			return Serializer.serializeObject(notification);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Notification fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Notification.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
