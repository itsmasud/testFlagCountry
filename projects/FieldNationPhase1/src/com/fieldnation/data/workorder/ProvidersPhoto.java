package com.fieldnation.data.workorder;

import com.fieldnation.json.annotations.Json;

public class ProvidersPhoto{
	@Json(name="user_id")
	private int _userId;
	@Json(name="photo_url")
	private String _photoUrl;
	@Json(name="lastname")
	private String _lastname;
	@Json(name="state")
	private String _state;
	@Json(name="firstname")
	private String _firstname;
	@Json(name="city")
	private String _city;

	public ProvidersPhoto(){
	}
	public int getUserId(){
		return _userId;
	}

	public String getPhotoUrl(){
		return _photoUrl;
	}

	public String getLastname(){
		return _lastname;
	}

	public String getState(){
		return _state;
	}

	public String getFirstname(){
		return _firstname;
	}

	public String getCity(){
		return _city;
	}

}
