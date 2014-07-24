package com.fieldnation.j2j;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.utils.misc;

public class JavaField {
	private static final int TYPE_UNKNOWN = 0;
	private static final int TYPE_DOUBLE = 1;
	private static final int TYPE_INTEGER = 2;
	private static final int TYPE_STRING = 3;
	private static final int TYPE_OBJECT = 4;
	private static final int TYPE_BOOLEAN = 5;
	private static final int TYPE_ARRAY = 100;

	public String name;
	public int dataType = TYPE_UNKNOWN;
	public String dataTypeName = "Object";
	public boolean isObject = false;
	public boolean isArray = false;

	public JavaField(String name, Object data, String packageName) throws ParseException {
		this.name = name;

		// Log.println("Field: " + name);

		if (name.equals("dateEntered") && !"".equals(data)) {
			Log.println("BP");
		}

		dataType = getType(data, name, packageName);
		if (dataType >= TYPE_ARRAY) {
			isArray = true;
			dataType = dataType - TYPE_ARRAY;
		}

		switch (dataType) {
		case TYPE_UNKNOWN:
			dataTypeName = "Object";
			break;
		case TYPE_DOUBLE:
			dataTypeName = "Double";
			break;
		case TYPE_INTEGER:
			dataTypeName = "Integer";
			break;
		case TYPE_STRING:
			dataTypeName = "String";
			break;
		case TYPE_OBJECT:
			isObject = true;
			JavaObject obj = JavaObject.getInstance(packageName, name);
			dataTypeName = obj.getClassName();
			break;
		case TYPE_BOOLEAN:
			dataTypeName = "Boolean";
			break;
		}
	}

	private static int getType(Object source, String name, String packageName) throws ParseException {

		if (source == null) {
			return TYPE_UNKNOWN;
		} else if (source.equals("")) {
			return TYPE_UNKNOWN;

		} else if (source instanceof JsonObject) {
			// this is an object
			JavaObject obj = JavaObject.getInstance(packageName, name);
			obj.addData((JsonObject) source);
			return TYPE_OBJECT;

		} else if (source instanceof JsonArray) {
			int type = TYPE_UNKNOWN;
			JsonArray ja = (JsonArray) source;
			for (int i = 0; i < ja.size(); i++) {
				type = pickBest(type, getType(ja.get(i), name, packageName));
			}
			if (type < TYPE_ARRAY)
				return type + TYPE_ARRAY;
			return type;

		} else if (source instanceof String) {
			try {
				int par = Integer.parseInt((String) source);
				return TYPE_INTEGER;
			} catch (Exception e1) {
				try {
					double par = Double.parseDouble((String) source);
					return TYPE_DOUBLE;
				} catch (Exception e2) {
					try {
						if ("true".equals(source) || "false".equals(source)) {
							return TYPE_BOOLEAN;
						} else {
							return TYPE_STRING;
						}
					} catch (Exception e3) {
						return TYPE_STRING;
					}
				}
			}
		}
		return TYPE_UNKNOWN;
	}

	private static int pickBest(int t1, int t2) {
		// always move away from unknown
		if (t1 == TYPE_UNKNOWN) {
			return t2;
		}
		if (t2 == TYPE_UNKNOWN) {
			return t1;
		}

		if (t1 == TYPE_STRING) {
			return t1;
		}
		if (t2 == TYPE_STRING) {
			return t2;
		}

		// double is next most important
		if (t1 == TYPE_DOUBLE) {
			return t1;
		}
		if (t2 == TYPE_DOUBLE) {
			return t2;
		}

		// integer
		if (t1 == TYPE_INTEGER) {
			return t1;
		} else if (t2 == TYPE_INTEGER) {
			return t2;
		}

		// bool
		if (t1 == TYPE_BOOLEAN) {
			return t1;
		}
		if (t2 == TYPE_BOOLEAN) {
			return t2;
		}

		// string and object are next
		if (t1 == TYPE_OBJECT) {
			return t1;
		}
		if (t2 == TYPE_OBJECT) {
			return t2;
		}

		// they will both be strings.. *shrug*, pick one.
		return t1;
	}

	public static JavaField pickBest(JavaField field1, JavaField field2) {
		// always move away from unknown
		if (field1.dataType == TYPE_UNKNOWN) {
			return field2;
		}
		if (field2.dataType == TYPE_UNKNOWN) {
			return field1;
		}

		if (field1.dataType == TYPE_STRING) {
			return field1;
		}
		if (field2.dataType == TYPE_STRING) {
			return field2;
		}

		// double is next most important
		if (field1.dataType == TYPE_DOUBLE) {
			return field1;
		}
		if (field2.dataType == TYPE_DOUBLE) {
			return field2;
		}

		// integer
		if (field1.dataType == TYPE_INTEGER) {
			return field1;
		} else if (field2.dataType == TYPE_INTEGER) {
			return field2;
		}

		// bool
		if (field1.dataType == TYPE_BOOLEAN) {
			return field1;
		}
		if (field2.dataType == TYPE_BOOLEAN) {
			return field2;
		}

		// string and object are next
		if (field1.dataType == TYPE_OBJECT) {
			return field1;
		}
		if (field2.dataType == TYPE_OBJECT) {
			return field2;
		}

		// they will both be strings.. *shrug*, pick one.
		return field1;
	}

	public String getFieldName() {
		String[] splitted = name.split("_");

		String result = "_" + splitted[0];

		for (int i = 1; i < splitted.length; i++) {
			result += misc.capitalize(splitted[i]);
		}
		return result;
	}

	public String getGetterName() {
		String n = getFieldName().substring(1);
		n = n.substring(0, 1).toUpperCase() + n.substring(1);
		return "get" + n;

	}

	public String toGetter() {
		String res = "";
		if (isArray) {
			res = "\tpublic " + dataTypeName + "[] " + getGetterName() + "(){\r\n";
			res += "\t\treturn " + getFieldName() + ";\r\n";
			res += "\t}\r\n\r\n";
		} else {
			res = "\tpublic " + dataTypeName + " " + getGetterName() + "(){\r\n";
			res += "\t\treturn " + getFieldName() + ";\r\n";
			res += "\t}\r\n\r\n";
		}
		return res;
	}

	@Override
	public String toString() {
		String res = "";
		if (isArray) {
			res = "\t@Json(name=\"" + name + "\")\r\n";
			res += "\tprivate " + dataTypeName + "[] " + getFieldName() + ";\r\n";
		} else {
			res = "\t@Json(name=\"" + name + "\")\r\n";
			res += "\tprivate " + dataTypeName + " " + getFieldName() + ";\r\n";
		}
		return res;
	}
}
