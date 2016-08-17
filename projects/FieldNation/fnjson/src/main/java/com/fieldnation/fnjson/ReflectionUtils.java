package com.fieldnation.fnjson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Michael on 3/29/2016.
 */
public class ReflectionUtils {
    protected static String getFieldName(Field field, String def) {
        if (def.equals(""))
            return field.getName();

        return def;
    }

    protected static boolean hasInterface(Class<?> needle, Class<?> haystack) {
        Class<?>[] interfaces = haystack.getInterfaces();

        for (Class<?> face : interfaces) {
            if (face == needle) {
                return true;
            } else if (hasInterface(needle, face)) {
                return true;
            }
        }

        return false;
    }

    protected static Type getCollectionTemplateType(Class<?> clazz) {
        ParameterizedType superclass = (ParameterizedType) clazz.getGenericSuperclass();
        return superclass.getActualTypeArguments()[0];

    }

    protected static <T extends Annotation> T getAnnotation(Field field, Class<T> annotationClass) {
        try {
            Annotation[] annotations = field.getAnnotations();
            if (annotations != null) {
                for (Annotation annotation : annotations) {
                    if (annotationClass.getCanonicalName().equals(annotation.annotationType().getCanonicalName())) {
                        return (T) annotation;
                    }
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return null;
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

    protected static boolean isPrimitive(Class<?> clazz) {
        return _PRIMITIVES.contains(clazz);
    }

    protected static boolean isArray(Class<?> clazz) {
        return clazz.getComponentType() != null;
    }

    protected static boolean isCollection(Class<?> clazz) {
        return hasInterface(Collection.class, clazz);
    }

}
