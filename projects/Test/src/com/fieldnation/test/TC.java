package com.fieldnation.test;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.annotations.Json;
import com.fieldnation.json.annotations.CollectionParameterType;

public class TC {

	@Json(name = "myDouble")
	public double _myDouble = 684185.5687;

	@Json
	public float _myFloat = 54.1F;

	@Json
	public int _myInt = 684;

	@Json
	public long _myLong = 12;

	@Json
	public JsonObject _literal = null;

	@Json
	public TC2 _tc2 = new TC2();

	@Json
	@CollectionParameterType(param = TC2.class)
	public List<TC2> _list;

	@Json
	public TC2[] _myArray;

	public TC() {
		try {
			_literal = new JsonObject("{test:\"this is a test\"}");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		_list = new LinkedList<TC2>();
		System.out.println("TC.construct()");
	}

	public List<TC2> getTc2List() {
		return _list;
	}
}