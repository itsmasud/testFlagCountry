package com.fieldnation.wsb;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class JavaObject {
	public String name;
	public String packageName;
	public List<JavaField> _fields = new LinkedList<JavaField>();

	public JavaObject(String name, String packageName) {
		this.name = name;
		this.packageName = packageName;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("package " + packageName + ";\r\n\r\n");

		sb.append("import com.fieldnation.json.annotations.Json;\r\n\r\n");

		sb.append("public class " + name + "{\r\n");

		for (int i = 0; i < _fields.size(); i++) {
			sb.append(_fields.get(i).toString());
		}

		sb.append("\r\n");

		sb.append("\tpublic " + name + "(){\r\n\t}\r\n");

		for (int i = 0; i < _fields.size(); i++) {
			sb.append(_fields.get(i).toGetter());
		}
		sb.append("}\r\n");

		return sb.toString();
	}
}
