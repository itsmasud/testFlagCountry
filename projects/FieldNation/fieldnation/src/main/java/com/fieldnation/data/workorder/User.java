package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;
import com.fieldnation.utils.misc;

public class User {
	@Json(name="city")
	private String _city;
	@Json(name = "companyId")
	private Integer _companyId;
	@Json(name = "companyName")
	private String _companyName;
	@Json(name="email")
	private String _email;
	@Json(name = "firstName")
	private String _firstName;
	@Json(name = "firstname")
	private String _firstname;
	@Json(name = "lastName")
	private String _lastName;
	@Json(name = "lastname")
	private String _lastname;
	@Json(name="phone")
	private String _phone;
	@Json(name = "photoThumbUrl")
	private String _photoThumbUrl;
	@Json(name = "photoUrl")
	private String _photoUrl;
	@Json(name="state")
	private String _state;
	@Json(name = "userId")
	private Integer _userId;
	@Json(name = "userType")
	private String _userType;
	@Json(name = "user_id")
	private Integer _user_Id;

	public User() {
	}
	public String getCity(){
		return _city;
	}

	public Integer getCompanyId() {
		return _companyId;
	}

	public String getCompanyName() {
		return _companyName;
	}

	public String getEmail(){
		return _email;
	}

	public String getFirstName() {
		return _firstName;
	}

	public String getFirstname() {
		return _firstname;
	}

	public String getLastName() {
		return _lastName;
	}

	public String getLastname() {
		return _lastname;
	}

	public String getPhone(){
		return _phone;
	}

	public String getPhotoThumbUrl() {
		return _photoThumbUrl;
	}

	public String getPhotoUrl() {
		return _photoUrl;
	}

	public String getState(){
		return _state;
	}

	public Integer getUserId() {
		if (_user_Id != null)
			return _user_Id;
		return _userId;
	}

	public String getUserType() {
		return _userType;
	}

	public JsonObject toJson() {
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

	/*-*********************************************-*/
	/*-				User Generated Code				-*/
	/*-*********************************************-*/
	public String getFullName() {
		String firstName = getFirstname();
		String lastName = getLastname();
		if (misc.isEmptyOrNull(firstName))
			firstName = "";

		if (misc.isEmptyOrNull(lastName))
			lastName = "";

		return (firstName + " " + lastName).trim();
	}
}
