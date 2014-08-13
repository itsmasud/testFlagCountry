package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class User{
	@Json(name="user_id")
	private Integer _userId;
	@Json(name="companyId")
	private Integer _companyId;
	@Json(name="lastname")
	private String _lastname;
	@Json(name="companyName")
	private String _companyName;
	@Json(name="firstname")
	private String _firstname;
	@Json(name="photoThumbUrl")
	private String _photoThumbUrl;
	@Json(name="userType")
	private String _userType;
	@Json(name="photoUrl")
	private String _photoUrl;

	public User(){
	}
	public Integer getUserId(){
		return _userId;
	}

	public Integer getCompanyId(){
		return _companyId;
	}

	public String getLastname(){
		return _lastname;
	}

	public String getCompanyName(){
		return _companyName;
	}

	public String getFirstname(){
		return _firstname;
	}

	public String getPhotoThumbUrl(){
		return _photoThumbUrl;
	}

	public String getUserType(){
		return _userType;
	}

	public String getPhotoUrl(){
		return _photoUrl;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(User user) {
		try {
			return Serializer.serializeObject(user);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static User fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(User.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
