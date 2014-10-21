package com.fieldnation.json;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

public class JsonTokenizer {
	private String _source;
	private String _temp;

	public JsonTokenizer(String jsonData) {
		_source = jsonData;
		_temp = _source.trim();
	}

	protected String getTemp() {
		return _temp;
	}

	public boolean isNextTokenStartObject() {
		char c = _temp.charAt(0);

		return c == '{';
	}

	public boolean isNextTokenFinishObject() {
		char c = _temp.charAt(0);

		return c == '}';
	}

	public boolean isNextTokenStartArray() {
		char c = _temp.charAt(0);

		return c == '[';
	}

	public boolean isNextTokenFinishArray() {
		char c = _temp.charAt(0);

		return c == ']';
	}

	public boolean isNextTokenComma() {
		char c = _temp.charAt(0);

		return c == ',';
	}

	public boolean isNextTokenString() {
		char c = _temp.charAt(0);

		return isString(c);
	}

	public boolean isNextTokenNull() {
		return _temp.startsWith("null");
	}

	public String nextToken() {
		char c = _temp.charAt(0);

		if (isNextTokenNull()) {
			_temp = _temp.substring(4).trim();
			return "null";
		}

		if (isNextTokenString()) {
			return parseString();
		}

		_temp = _temp.substring(1).trim();

		return c + "";
	}

	protected Object parseValue() throws ParseException {
		if (isNextTokenNull()) {
			_temp = _temp.substring(4).trim();
			return JsonObject.JsonNULL;
		} else if (isNextTokenString()) {
			return parseString();
		} else if (isNextTokenStartObject()) {
			return new JsonObject(this);
		} else if (isNextTokenStartArray()) {
			return new JsonArray(this);
		}

		throw new ParseException("Could not detect object type! (" + _temp
				+ ")", _source.length() - _temp.length());
	}

	private String parseString() {
		char c;
		int i;
		StringBuilder key = new StringBuilder();
		c = _temp.charAt(0);
		if (isAlphaNumeric(c)) {
			i = 1;
			key.append(c);
			while (isAlphaNumeric((c = _temp.charAt(i)))) {
				key.append(c);
				i++;
			}

			_temp = _temp.substring(i).trim();
		} else if (c == '"') {
			key = new StringBuilder();
			i = 1;
			while ((c = _temp.charAt(i)) != '"') {
				if (c == '\\') {
					i++;
					c = _temp.charAt(i);
					switch (c) {
					case '"':
						key.append("\"");
						break;
					case '\\':
						key.append("\\");
						break;
					case 'b':
						key.append((char) 0x08);
						break;
					case 'f':
						key.append((char) 0x0C);
						break;
					case 'n':
						key.append("\n");
						break;
					case 'r':
						key.append("\r");
						break;
					case 't':
						key.append("\t");
						break;
					case 'u':
						// TODO Parse unicode chars here
						key.append("u");
						break;
					default:
						key.append(c);
					}

					// key += _temp.charAt(i);
				} else {
					key.append(c);
				}
				i++;
			}

			_temp = _temp.substring(i + 1).trim();
		}

		return key.toString();
	}

	public static String escapeString(String src) {
		src = src.replaceAll("\\x5c", "\\\\\\\\");
		src = src.replaceAll("\\x22", "\\\\\\\"");
		src = src.replaceAll("\\x08", "\\\\b");
		src = src.replaceAll("\\x0C", "\\\\f");
		src = src.replaceAll("\\x0A", "\\\\n");
		src = src.replaceAll("\\x0D", "\\\\r");
		src = src.replaceAll("\\x09", "\\\\t");

		return '"' + src + '"';
	}

	public static boolean isAlphaNumeric(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
				|| (c >= '0' && c <= '9') || c == '-' || c == '.' || c == '_';
	}

	public static boolean isString(char c) {
		return c == '"' || isAlphaNumeric(c);
	}

	public static String repeat(String value, int count) {
		StringBuilder data = new StringBuilder();
		for (int i = 0; i < count; i++) {
			data.append(value);
		}
		return data.toString();
	}

	public static List<String> parsePath(String path) {
		List<String> directions = new LinkedList<String>();

		StringBuilder temp = new StringBuilder();

		for (int i = 0; i < path.length(); i++) {
			char c = path.charAt(i);

			if (c == '.') {
				if (temp.length() > 0)
					directions.add(temp.toString());
				temp = new StringBuilder();
			} else if (c == '[') {
				if (temp.length() > 0)
					directions.add(temp.toString());
				temp = new StringBuilder();

				while (c != ']') {
					temp.append(c);
					i++;
					c = path.charAt(i);
				}
				temp.append(c);

				if (temp.length() > 0)
					directions.add(temp.toString());
				temp = new StringBuilder();
			} else if (c == '"' || c == '\'') {
				i++;
				c = path.charAt(i);

				while (c != '\'' && c != '"') {
					temp.append(c);
					i++;
					c = path.charAt(i);
				}

				if (temp.length() > 0)
					directions.add(temp.toString());
				temp = new StringBuilder();
				i++;
			} else {
				temp.append(c);
			}
		}

		if (temp.length() > 0)
			directions.add(temp.toString());

		return directions;
	}

	public static Object walkPath(Object obj, List<String> directions)
			throws ParseException {

		if (directions.size() == 0)
			return obj;

		String item = directions.remove(0);

		if (obj instanceof JsonObject) {
			JsonObject job = (JsonObject) obj;

			Object no = job.get(item);

			return walkPath(no, directions);
		} else if (obj instanceof JsonArray) {
			JsonArray ja = (JsonArray) obj;

			Object no = ja.get(Integer.parseInt(item));

			return walkPath(no, directions);
		}

		throw new ParseException("Path not found in object", directions.size());
	}

	public static boolean isInt(String test) {
		try {
			Integer.parseInt(test);
			return true;
		} catch (Exception ex) {
		}
		return false;
	}
}
