package com.fieldnation.test;

import java.text.ParseException;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;

public class TestJson {

	public static void main(String[] args) {
		new TestJson();
	}

	public TestJson() {
		TC tc = new TC();

		try {
			JsonObject json = Serializer.serializeObject(tc);
			System.out.println(json.display());
		} catch (IllegalArgumentException | IllegalAccessException
				| ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JsonObject json = new JsonObject(
					"{ \"_myLong\":12, \"_literal\":{ \"test\":\"this is a test\" }, \"_tc2\":{ \"_myDouble\":23.584 }, \"_myFloat\":54.1, \"_myArray\":null, \"_list\":[ ], \"myDouble\":684185.5687, \"_myInt\":684}");
			tc = Serializer.unserializeObject(TC.class, json);

			System.out.println("BP");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
