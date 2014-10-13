package com.fieldnation.data.profile;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Profile{
	@Json(name="city")
	private String _city;
	@Json(name="description")
	private String _description;
	@Json(name="email")
	private String _email;
	@Json(name="firstname")
	private String _firstname;
	@Json(name="lastname")
	private String _lastname;
	@Json(name="marketplaceStatusOn")
	private Boolean _marketplaceStatusOn;
	@Json(name="newNotificationCount")
	private Integer _newNotificationCount;
	@Json(name="phone")
	private String _phone;
	@Json(name="photo")
	private Photo _photo;
	@Json(name="rating")
	private Double _rating;
	@Json(name="ratingsTotal")
	private Integer _ratingsTotal;
	@Json(name="state")
	private String _state;
	@Json(name="tagline")
	private String _tagline;
	@Json(name="unreadMessageCount")
	private Integer _unreadMessageCount;
	@Json(name="userId")
	private Integer _userId;
	@Json(name="workordersTotal")
	private Integer _workordersTotal;

	public Profile(){
	}
	public String getCity(){
		return _city;
	}

	public String getDescription(){
		return _description;
	}

	public String getEmail(){
		return _email;
	}

	public String getFirstname(){
		return _firstname;
	}

	public String getLastname(){
		return _lastname;
	}

	public Boolean getMarketplaceStatusOn(){
		return _marketplaceStatusOn;
	}

	public Integer getNewNotificationCount(){
		return _newNotificationCount;
	}

	public String getPhone(){
		return _phone;
	}

	public Photo getPhoto(){
		return _photo;
	}

	public Double getRating(){
		return _rating;
	}

	public Integer getRatingsTotal(){
		return _ratingsTotal;
	}

	public String getState(){
		return _state;
	}

	public String getTagline(){
		return _tagline;
	}

	public Integer getUnreadMessageCount(){
		return _unreadMessageCount;
	}

	public Integer getUserId(){
		return _userId;
	}

	public Integer getWorkordersTotal(){
		return _workordersTotal;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(Profile profile) {
		try {
			return Serializer.serializeObject(profile);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Profile fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Profile.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
