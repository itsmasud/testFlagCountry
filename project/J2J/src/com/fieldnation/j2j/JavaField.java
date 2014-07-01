package com.fieldnation.j2j;

public class JavaField {

	public String name;
	public String dataType;
	public boolean isArray;

	public JavaField(String name, String dataType, boolean isArray) {
		this.name = name;
		this.dataType = dataType;
		this.isArray = isArray;
	}

	public String getJavaName() {
		String[] splitted = name.split("_");

		String result = "_" + splitted[0];

		for (int i = 1; i < splitted.length; i++) {
			result += splitted[i].substring(0, 1).toUpperCase() + splitted[i].substring(1);
		}

		return result;
	}

	public String getGetterName() {
		String n = getJavaName().substring(1);
		n = n.substring(0, 1).toUpperCase() + n.substring(1);
		return "get" + n;

	}

	public String toGetter() {
		String res = "";
		if (isArray) {
			res = "\tpublic " + dataType + "[] " + getGetterName() + "(){\r\n";
			res += "\t\treturn " + getJavaName() + ";\r\n";
			res += "\t}\r\n\r\n";
		} else {
			res = "\tpublic " + dataType + " " + getGetterName() + "(){\r\n";
			res += "\t\treturn " + getJavaName() + ";\r\n";
			res += "\t}\r\n\r\n";
		}
		return res;
	}

	@Override
	public String toString() {
		String res = "";
		if (isArray) {
			res = "\t@Json(name=\"" + name + "\")\r\n";
			res += "\tprivate " + dataType + "[] " + getJavaName() + ";\r\n";
		} else {
			res = "\t@Json(name=\"" + name + "\")\r\n";
			res += "\tprivate " + dataType + " " + getJavaName() + ";\r\n";
		}
		return res;
	}
}
