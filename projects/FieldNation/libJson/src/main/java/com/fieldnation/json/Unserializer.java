package com.fieldnation.json;

import com.fieldnation.json.annotations.CollectionParameterType;
import com.fieldnation.json.annotations.Json;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Created by Michael on 3/29/2016.
 * <p/>
 * This is a static class used to unserialize JsonObjects into real Java objects
 */
public class Unserializer {
    /**
     * Instantiates targetClass and popultes the fields within that class based on the contents of
     * the JsonObject.
     *
     * @param targetClazz the class to create
     * @param source      the Json that represents the class
     * @param <T>         the type to return. It should match targetClass
     * @return the populated class
     * @throws Exception A million things can go wrong with reflection or the contextents of the
     *                   JsonObject, this could be any number of issues.
     */
    public static <T> T unserializeObject(Class<T> targetClazz, JsonObject source) throws Exception {
        long start = System.currentTimeMillis();

        T t = unserializeObject(targetClazz, source, true);

        System.out.println("Unserializer.unserializeObject " + targetClazz.getName() + " time: " + (System.currentTimeMillis() - start));
        return t;
    }

    public static <T> T unserializeObject(Class<T> targetClazz, JsonObject source, boolean junk) throws Exception {
        // Instantiate target class
        Constructor<T> targetConstructor = targetClazz.getConstructor((Class<?>[]) null);
        T dest = targetConstructor.newInstance();

        // Read all of its fields
        Field[] targetFields = targetClazz.getDeclaredFields();

        for (Field targetField : targetFields) {
            targetField.setAccessible(true);

            // check annotation. if it doesn't exist then skip
            Json targetFieldAnnotation = ReflectionUtils.getAnnotation(targetField, Json.class);
            if (targetFieldAnnotation == null)
                continue;

            // generate the name of the field as it would appear in the JSON
            String targetFieldJsonName = ReflectionUtils.getFieldName(targetField, targetFieldAnnotation.name());

            // get the field's class type
            Class<?> targetFieldClazz = targetField.getType();

            // if this object is a collection, then get the parameter type for the collection
            CollectionParameterType collectionTypeAnno = ReflectionUtils.getAnnotation(targetField, CollectionParameterType.class);
            Class<?> collectionParamType = null;
            if (collectionTypeAnno != null)
                collectionParamType = collectionTypeAnno.param();

            try {
                // check that the data exists in JSON
                if (source.has(targetFieldJsonName) && source.get(targetFieldJsonName) != null) {
                    Object fieldValue = null;
                    try {
                        // parse the contents
                        fieldValue = unserialize(targetFieldClazz, source.get(targetFieldJsonName), targetField.get(dest), collectionParamType);
                    } catch (Exception ex) {
                        System.out.println("Failure parsing " + targetClazz.getName() + ":" + targetFieldJsonName);
                        ex.printStackTrace();
                    }

                    // set the value of the field
                    targetField.set(dest, fieldValue);
                }
            } catch (Exception ex) {
                System.out.println("Failure parsing " + targetClazz.getName() + ":" + targetFieldJsonName);
                throw new Exception("Failure parsing " + targetClazz.getName() + ":" + targetFieldJsonName, ex);
            }
        }
        return dest;
    }

    /**
     * Tries to convert the source object into the targetClass and return the results.
     *
     * @param targetClass
     * @param source
     * @param defaultValue
     * @param collectionType
     * @return
     * @throws Exception
     */
    private static Object unserialize(Class<?> targetClass, Object source, Object defaultValue, Class<?> collectionType) throws Exception {
        if (ReflectionUtils.isPrimitive(targetClass)) {
            return unserializePrimitive(targetClass, source.toString());
        }

        if (ReflectionUtils.isArray(targetClass)) {
            return unserializeArray(targetClass, (JsonArray) source);
        }

        if (ReflectionUtils.isCollection(targetClass)) {
            return unserializeCollection((JsonArray) source, (Collection<Object>) defaultValue, collectionType);
        }

        if (targetClass == JsonArray.class) {
            return source;
        }

        if (targetClass == JsonObject.class) {
            return source;
        }

        // this object is none of the above. try unserializing the object it represents
        return unserializeObject(targetClass, (JsonObject) source, true);
    }

    /**
     * Tries to parse the source string into a primitive type
     * <p/>
     * Supported Types
     * * Float/float
     * * Boolean/boolean
     * * Character/char
     * * Byte/byte
     * * Short/short
     * * Integer/int
     * * Long/long
     * * Double/double
     * * String
     *
     * @param targetClazz the primitive type that we need to create
     * @param source      the string representing the data
     * @return the object parsed from the string
     * @throws UnsupportedDataTypeException
     */
    private static Object unserializePrimitive(Class<?> targetClazz, String source) throws UnsupportedDataTypeException {
        if (source == null) {
            return null;
        }

        if (source.equals("")) {
            return null;
        }

        if (targetClazz == Float.class || targetClazz == float.class) {
            return Float.parseFloat(source);
        }

        if (targetClazz == Boolean.class || targetClazz == boolean.class) {
            return "true".equals(source.toLowerCase());
        }
        if (targetClazz == Character.class || targetClazz == char.class) {
            return source.charAt(0);
        }
        if (targetClazz == Byte.class || targetClazz == byte.class) {
            return source.getBytes()[0];
        }
        if (targetClazz == Short.class || targetClazz == short.class) {
            return Short.parseShort(source);
        }
        if (targetClazz == Integer.class || targetClazz == int.class) {
            return Integer.parseInt(source);
        }
        if (targetClazz == Long.class || targetClazz == long.class) {
            return Long.parseLong(source);
        }
        if (targetClazz == Double.class || targetClazz == double.class) {
            return Double.parseDouble(source);
        }
        if (targetClazz == String.class) {
            return source;
        }

        throw new UnsupportedDataTypeException(targetClazz.getName());
    }

    /**
     * Creates an array of type targetClass and tries to fill it with obejcts unserialized from source
     *
     * @param targetClass
     * @param source
     * @return
     * @throws Exception
     */
    private static Object unserializeArray(Class<?> targetClass, JsonArray source) throws Exception {
        Class<?> componentType = targetClass.getComponentType();

        // Object[] dest = new Object[source.size()];
        Object[] targetArray = (Object[]) Array.newInstance(componentType, source.size());
        for (int i = 0; i < source.size(); i++) {
            // dest.add(unserialize(compType, source.get(i), null, null));
            targetArray[i] = unserialize(componentType, source.get(i), null, null);
        }
        return targetArray;
    }

    private static Object unserializeCollection(JsonArray source, Collection<Object> defaultValue, Class<?> collectionParamType) throws Exception {
        if (collectionParamType == null) {
            throw new IllegalArgumentException(
                    "collectionParamType must be a class. Check that CollectionParameterType annotation is set for any collections you may have.");
        }

        if (defaultValue == null) {
            throw new IllegalArgumentException(
                    "defaultValue was null. Check that your collection fields have a non-null initialization parameter.");
        }
        // Type param = getCollectionTemplateType(init.getClass());

        for (int i = 0; i < source.size(); i++) {
            defaultValue.add(unserialize(collectionParamType, source.get(i), null, null));
        }
        return defaultValue;
    }
}
