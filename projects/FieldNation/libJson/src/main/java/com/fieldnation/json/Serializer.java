package com.fieldnation.json;

import com.fieldnation.json.annotations.CollectionParameterType;
import com.fieldnation.json.annotations.Json;

import java.lang.annotation.Annotation;
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

public class Serializer {

    public static JsonObject serializeObject(Object source) throws ParseException, IllegalArgumentException, IllegalAccessException {
        JsonObject dest = new JsonObject();
        // get the source object's fields
        Field[] fields = source.getClass().getDeclaredFields();

        // with each field...
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);

            // check that it's annotated for us
            Json anno = ReflectionUtils.getAnnotation(field, Json.class);
            if (anno == null)
                continue;

            // generate the json name
            String jname = ReflectionUtils.getFieldName(field, anno.name());

            dest.put(jname, serialize(field.get(source)));
        }
        return dest;
    }

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
        if (ReflectionUtils.isPrimitive(clazz)) {
            return source;
        }
        if (ReflectionUtils.isCollection(clazz)) {
            return serializeCollection((Collection<Object>) source);
        }
        if (ReflectionUtils.isArray(clazz)) {
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
}
