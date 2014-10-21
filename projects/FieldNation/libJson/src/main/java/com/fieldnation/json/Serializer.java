package com.fieldnation.json;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import com.fieldnation.json.annotations.Json;
import com.fieldnation.json.annotations.CollectionParameterType;

public class Serializer {

	/*-			To Json Stuff			-*/
	private static Object serialize(Object source) throws IllegalArgumentException, IllegalAccessException, ParseException {
		if (source == null)
			return source;

		Class<?> clazz = source.getClass();
		if (source instanceof JsonObject) {
			return source;
		}
		if (source instanceof JsonArray) {
			return source;
		}
		if (isPrimitive(clazz)) {
			return source;
		}
		if (isCollection(clazz)) {
			return serializeCollection((Collection<Object>) source);
		}
		if (isArray(clazz)) {
			return serializeArray((Object[]) source);
		}

		return serializeObject(source);
	}

	private static JsonArray serializeCollection(Collection<Object> collection) throws IllegalArgumentException, IllegalAccessException, ParseException {
		JsonArray dest = new JsonArray();

		Iterator<Object> iter = collection.iterator();
		while (iter.hasNext()) {
			dest.add(serialize(iter.next()));
		}
		return dest;
	}

	private static JsonArray serializeArray(Object[] array) throws IllegalArgumentException, IllegalAccessException, ParseException {
		JsonArray dest = new JsonArray();

		for (int i = 0; i < array.length; i++) {
			dest.add(serialize(array[i]));
		}
		return dest;
	}

	public static JsonObject serializeObject(Object source) throws ParseException, IllegalArgumentException, IllegalAccessException {
		JsonObject dest = new JsonObject();
		// get the source object's fields
		Field[] fields = source.getClass().getDeclaredFields();

		// with each field...
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);

			// check that it's annotated for us
			Json anno = field.getAnnotation(Json.class);
			if (anno == null)
				continue;

			// generate the json name
			String jname = getFieldName(field, anno.name());

			dest.put(jname, serialize(field.get(source)));
		}
		return dest;
	}

	/*-			From JsonStuff			-*/
	private static Object unserialize(Class<?> destClass, Object source, Object init, Class<?> paramType) throws Exception {
		if (isPrimitive(destClass)) {
			return unserializePrimitive(destClass, source.toString());
		}
		if (isArray(destClass)) {
			return unserializeArray(destClass, (JsonArray) source);
		}
		if (isCollection(destClass)) {
			return unserializeCollection(destClass, (JsonArray) source, (Collection<Object>) init, paramType);
		}
		return unserializeObject(destClass, (JsonObject) source);
	}

	private static Object unserializePrimitive(Class<?> clazz, String source) throws UnsupportedDataTypeException {
		if (source == null) {
			return null;
		}

		if (source.equals("")) {
			return null;
		}

		if (clazz == Float.class || clazz == float.class) {
			return Float.parseFloat(source);
		}
		if (clazz == Boolean.class || clazz == boolean.class) {
			return "true".equals(source.toLowerCase());
		}
		if (clazz == Character.class || clazz == char.class) {
			return source.charAt(0);
		}
		if (clazz == Byte.class || clazz == byte.class) {
			return source.getBytes()[0];
		}
		if (clazz == Short.class || clazz == short.class) {
			return Short.parseShort(source);
		}
		if (clazz == Integer.class || clazz == int.class) {
			return Integer.parseInt(source);
		}
		if (clazz == Long.class || clazz == long.class) {
			return Long.parseLong(source);
		}
		if (clazz == Double.class || clazz == double.class) {
			return Double.parseDouble(source);
		}
		if (clazz == String.class) {
			return source;
		}

		throw new UnsupportedDataTypeException(clazz.getName());
	}

	private static Object unserializeArray(Class<?> destClass, JsonArray source) throws Exception {
		Class<?> compType = destClass.getComponentType();

		// Object[] dest = new Object[source.size()];
		Object[] dest = (Object[]) Array.newInstance(compType, source.size());
		for (int i = 0; i < source.size(); i++) {
			// dest.add(unserialize(compType, source.get(i), null, null));
			dest[i] = unserialize(compType, source.get(i), null, null);
		}
		return dest;
	}

	private static Object unserializeCollection(Class<?> destClass, JsonArray source, Collection<Object> init,
			Class<?> paramType) throws Exception {

		if (paramType == null) {
			throw new IllegalArgumentException(
					"paramType must be a class. Check that CollectionParameterType annotation is set for any collections you may have.");
		}
		// Type param = getCollectionTemplateType(init.getClass());

		for (int i = 0; i < source.size(); i++) {
			init.add(unserialize(paramType, source.get(i), null, null));
		}
		return init;
	}

	public static <T> T unserializeObject(Class<T> clazz, JsonObject source) throws Exception {
		Constructor<T> c = clazz.getConstructor((Class<?>[]) null);
		// System.out.println("Class: " + clazz.getName());
		T dest = c.newInstance();

		Field[] fields = clazz.getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);

			Json anno = field.getAnnotation(Json.class);
			if (anno == null)
				continue;

			String jname = getFieldName(field, anno.name());
			Class<?> fieldclass = field.getType();

			CollectionParameterType collectionParameterType = field.getAnnotation(CollectionParameterType.class);
			try {
				if (source.has(jname) && source.get(jname) != null) {
					// System.out.println("Parsing " + clazz.getName() + ":" +
					// jname);
					Object value = null;
					try {
						if (collectionParameterType != null) {
							value = unserialize(fieldclass, source.get(jname), field.get(dest),
									collectionParameterType.param());
						} else {
							value = unserialize(fieldclass, source.get(jname), field.get(dest), null);
						}
					} catch (Exception ex) {
						System.out.println("Failure parsing " + clazz.getName() + ":" + jname);
						ex.printStackTrace();
					}

					field.set(dest, value);
				}
			} catch (Exception ex) {
				System.out.println("Failure parsing " + clazz.getName() + ":" + jname);
				throw new Exception("Failure parsing " + clazz.getName() + ":" + jname, ex);
			}

		}

		return dest;
	}

	/* utils */
	private static boolean isArray(Class<?> clazz) {
		return clazz.getComponentType() != null;
	}

	private static boolean isCollection(Class<?> clazz) {
		return hasInterface(Collection.class, clazz);
	}

	private static String getFieldName(Field field, String def) {
		if (def.equals(""))
			return field.getName();

		return def;
	}

	private static boolean hasInterface(Class<?> needle, Class<?> haystack) {
		Class<?>[] interfaces = haystack.getInterfaces();

		for (int i = 0; i < interfaces.length; i++) {
			if (interfaces[i] == needle) {
				return true;
			} else if (hasInterface(needle, interfaces[i])) {
				return true;
			}
		}

		return false;
	}

	private static Type getCollectionTemplateType(Class<?> clazz) {
		ParameterizedType superclass = (ParameterizedType) clazz.getGenericSuperclass();
		return superclass.getActualTypeArguments()[0];

	}

	private static final Set<Class<?>> _PRIMITIVES = getPrimitives();

	private static Set<Class<?>> getPrimitives() {
		Set<Class<?>> p = new HashSet<Class<?>>();
		p.add(Boolean.class);
		p.add(Character.class);
		p.add(Byte.class);
		p.add(Short.class);
		p.add(Integer.class);
		p.add(Long.class);
		p.add(Float.class);
		p.add(Double.class);
		p.add(String.class);
		p.add(char.class);
		p.add(byte.class);
		p.add(int.class);
		p.add(long.class);
		p.add(boolean.class);
		p.add(double.class);
		p.add(float.class);
		p.add(short.class);
		return p;
	}

	private static boolean isPrimitive(Class<?> clazz) {
		return _PRIMITIVES.contains(clazz);
	}

}
