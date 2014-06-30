package com.fieldnation.wsb;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;

public class JavaObject {
	public String name;
	public String packageName;
	public List<JavaField> _fields = new LinkedList<JavaField>();

	public JavaObject(String name, String packageName) {
		this.name = name;
		this.packageName = packageName;
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

		for (int i = 0; i < _fields.size(); i++) {
			sb.append(_fields.get(i).toString());
		}

		sb.append("\r\n");

		sb.append("	public " + name + "(){\r\n	}\r\n");

		for (int i = 0; i < _fields.size(); i++) {
			sb.append(_fields.get(i).toGetter());
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
}
