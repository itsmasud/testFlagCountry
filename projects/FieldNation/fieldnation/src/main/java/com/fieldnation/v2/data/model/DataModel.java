package com.fieldnation.v2.data.model;

import com.fieldnation.fnjson.JsonTokenizer;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by mc on 6/16/17.
 */

public class DataModel {


    public static boolean has(Object object, String path) {
        if (object == null)
            return false;

        List<String> directions = JsonTokenizer.parsePath(path);

        Object cursor = object;
        for (String direction : directions) {
            cursor = getSingle(cursor, direction);
            if (cursor == null)
                return false;
        }
        return true;
    }

    private static Object getSingle(Object object, String methodName) {
        try {
            Class<?> clazz = object.getClass();

            Method method = clazz.getMethod(methodName, null);

            return method.invoke(object, null);
        } catch (Exception ex) {
            return null;
        }
    }
}
