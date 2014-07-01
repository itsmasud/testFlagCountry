package com.fieldnation.j2j;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Enumeration;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;

public class Serializer {
	private String _packageName;
	private String _outputPath;

	public Serializer(String packageName, String outputPath) {
		_packageName = packageName;
		_outputPath = outputPath;
	}

	public void performJ2J(String data, String rootname) {
		try {
			JsonObject json = new JsonObject(data);
			performJ2J(json, rootname);
		} catch (Exception ex) {
			try {
				JsonArray ja = new JsonArray(data);
				performJ2J(ja, rootname);
			} catch (Exception ex1) {
				ex1.printStackTrace();
			}
		}
	}

	private void performJ2J(JsonObject jsonSource, String className) throws IOException, ParseException {
		FileOutputStream fout = new FileOutputStream(
				_outputPath + "/" + className + ".java");

		JavaObject obj = new JavaObject(className, _packageName);

		Enumeration<String> keys = jsonSource.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			Object j = jsonSource.get(key);
			JavaField field = null;

			if (j instanceof JsonObject) {
				field = new JavaField(key, formatForClassName(key), false);
				performJ2J((JsonObject) j, formatForClassName(key));
			} else if (j instanceof JsonArray) {
				field = new JavaField(key, formatForClassName(key), true);
				performJ2J((JsonArray) j, formatForClassName(key));
			} else {
				try {
					int par = jsonSource.getInt(key);
					field = new JavaField(key, "Integer", false);
				} catch (Exception ex) {
					try {
						double par = jsonSource.getDouble(key);
						field = new JavaField(key, "Double", false);
					} catch (Exception ex1) {
						try {
							String str = jsonSource.getString(key);
							if ("true".equals(str) || "false".equals(str)) {
								field = new JavaField(key, "Boolean", false);
							} else {
								field = new JavaField(key, "String", false);
							}
						} catch (Exception ex2) {
							field = new JavaField(key, "String", false);
						}
					}
				}
			}
			obj._fields.add(field);
		}

		fout.write(obj.toString().getBytes());
		fout.close();

	}

	private String formatForClassName(String value) {
		String[] splitted = value.split("_");

		String result = "";

		for (int i = 0; i < splitted.length; i++) {
			result += splitted[i].substring(0, 1).toUpperCase() + splitted[i].substring(1);
		}

		return result;
	}

	private void performJ2J(JsonArray jsonSource, String className) throws ParseException, IOException {
		JsonObject merged = mergeArray(jsonSource);
		performJ2J(merged, className);
	}

	private JsonObject mergeArray(JsonArray jsonSource) throws ParseException {
		JsonObject output = new JsonObject();

		for (int i = 0; i < jsonSource.size(); i++) {
			JsonObject item = jsonSource.getJsonObject(i);
			output.deepmerge(item);
		}

		return output;
	}
}
