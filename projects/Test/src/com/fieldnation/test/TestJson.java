package com.fieldnation.test;

import java.text.ParseException;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;

public class TestJson {

	public static void main(String[] args) {
		new TestJson();
	}

	public TestJson() {
		
		System.out.println( Integer.parseInt("1.1"));
		
		TC tc = new TC();

		tc._list.add(new TC2(123));
		tc._list.add(new TC2(456));
		tc._list.add(new TC2(789));
		tc._list.add(new TC2(0));
		
		tc._myArray = new TC2[4];
		tc._myArray[0] = new TC2(12);
		tc._myArray[1] = new TC2(23);
		tc._myArray[2] = new TC2(34);
		tc._myArray[3] = new TC2(45);

		String jsonString = "";
		try {
			JsonObject json = Serializer.serializeObject(tc);
			System.out.println(json.display());
			jsonString = json.toString();
		} catch (IllegalArgumentException | IllegalAccessException
				| ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JsonObject json = new JsonObject(jsonString);
			tc = Serializer.unserializeObject(TC.class, json);

			System.out.println(Serializer.serializeObject(tc).toString());

			System.out.println(jsonString.equals(Serializer.serializeObject(
					tc).toString()));

			System.out.println("BP");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
