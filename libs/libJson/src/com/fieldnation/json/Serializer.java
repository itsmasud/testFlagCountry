package com.fieldnation.json;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

public class Serializer {

	public static <T> T fromJsonObject(JsonObject json, Class<T> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ParseException {
		T dest = clazz.getConstructor((Class<?>) null).newInstance(
				(Object[]) null);

		Field[] fields = clazz.getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			JsonField jfield = field.getAnnotation(JsonField.class);

			// skip field if no annotation
			if (jfield == null) {
				continue;
			}

			String fname = jfield.name();

			if (json.has(fname)) {
				Class<?> fieldClazz = field.getType();
				//field.set(dest, value);
			}
		}

		return dest;
	}
}
