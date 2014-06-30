package com.fieldnation.data.workorder;

import com.fieldnation.json.annotations.Json;

public class Skillsets{
	@Json(name="name")
	private String _name;
	@Json(name="dynamic_term_id")
	private int _dynamicTermId;

	public Skillsets(){
	}
	public String getName(){
		return _name;
	}

	public int getDynamicTermId(){
		return _dynamicTermId;
	}

}
