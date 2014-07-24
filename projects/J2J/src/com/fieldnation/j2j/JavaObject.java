package com.fieldnation.j2j;

import java.text.ParseException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.utils.misc;

public class JavaObject {
	private static final Hashtable<String, JavaObject> _objectRegistry = new Hashtable<String, JavaObject>();

	public String name;
	public String packageName;
	public Hashtable<String, JavaField> _fields = new Hashtable<String, JavaField>();

	public static JavaObject getInstance(String packageName, String name) {
		String classname = formatClassName(name);
		if (!_objectRegistry.containsKey(packageName + "." + classname)) {
			_objectRegistry.put(packageName + "." + classname, new JavaObject(packageName, classname));
		}

		return _objectRegistry.get(packageName + "." + classname);
	}

	public static void cleanup() {
		_objectRegistry.clear();
	}

	public static Enumeration<JavaObject> getJavaObjects() {
		return _objectRegistry.elements();
	}

	private JavaObject(String packageName, String name) {
		this.name = name;
		this.packageName = packageName;
	}

	public void addData(JsonObject source) throws ParseException {
		Enumeration<String> keys = source.keys();

		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			Object value = source.get(key);

			JavaField newField = new JavaField(key, value, packageName);

			if (_fields.containsKey(key)) {
				newField = JavaField.pickBest(newField, _fields.get(key));
			}

			_fields.put(key, newField);
		}

	}

	private String getFieldName() {
		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("package " + packageName + ";\r\n\r\n");

		sb.append("import com.fieldnation.json.JsonObject;\r\n");
		sb.append("import com.fieldnation.json.Serializer;\r\n");
		sb.append("import com.fieldnation.json.annotations.Json;\r\n\r\n");

		sb.append("public class " + name + "{\r\n");

		Enumeration<String> keys = _fields.keys();
		while (keys.hasMoreElements()) {
			sb.append(_fields.get(keys.nextElement()).toString());
		}

		sb.append("\r\n");

		sb.append("	public " + name + "(){\r\n	}\r\n");

		keys = _fields.keys();
		while (keys.hasMoreElements()) {
			sb.append(_fields.get(keys.nextElement()).toGetter());
		}

		sb.append("	public JsonObject toJson(){\r\n");
		sb.append("		return toJson(this);\r\n");
		sb.append("	}\r\n\r\n");

		sb.append("	public static JsonObject toJson(" + name + " " + getFieldName() + ") {\r\n");
		sb.append("		try {\r\n");
		sb.append("			return Serializer.serializeObject(" + getFieldName() + ");\r\n");
		sb.append("		} catch (Exception ex) {\r\n");
		sb.append("			ex.printStackTrace();\r\n");
		sb.append("			return null;\r\n");
		sb.append("		}\r\n");
		sb.append("	}\r\n\r\n");

		sb.append("	public static " + name + " fromJson(JsonObject json) {\r\n");
		sb.append("		try {\r\n");
		sb.append("			return Serializer.unserializeObject(" + name + ".class, json);\r\n");
		sb.append("		} catch (Exception ex) {\r\n");
		sb.append("			ex.printStackTrace();\r\n");
		sb.append("			return null;\r\n");
		sb.append("		}\r\n");
		sb.append("	}\r\n\r\n");

		sb.append("}\r\n");

		return sb.toString();
	}

	public String getClassName() {
		return formatClassName(name);
	}

	public static String formatClassName(String value) {
		String[] splitted = value.split("_");

		String result = "";

		for (int i = 0; i < splitted.length; i++) {
			result += misc.capitalize(splitted[i]);
		}

		if (Config.ClassNameMap.containsKey(result))
			return Config.ClassNameMap.get(result);

		return result;
	}

	public String getUnderscoreFields() {
		StringBuilder sb = new StringBuilder();

		sb.append("Object: " + name + "\r\n");
		Enumeration<String> keys = _fields.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			JavaField field = _fields.get(key);
			if (field.name.contains("_")) {
				sb.append("\t" + field.name + "\r\n");
			}
		}

		return sb.toString();
	}
}
