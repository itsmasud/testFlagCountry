package com.fieldnation.test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedList;

public class CheckAnno {

	public static void main(String[] args) {
		try {
			String c = "a";
			Character ch = c.charAt(0);
			
			String source = "1.0";
			Class<?> clazz = float.class;

			if (clazz == Float.class || clazz == float.class) {
				System.out.println(Float.parseFloat(source));
			}

			Field field = TC.class.getDeclaredField("_list");
			System.out.println(Float.class.isInstance(1.0F));

			System.out.println(Float.class.isAssignableFrom(float.class));
			System.out.println(float.class.isAssignableFrom(Float.class));
			if (Collection.class.isAssignableFrom(field.getType())) {
				System.out.println(field.getType());
				System.out.println(field.getType().getComponentType());
				ParameterizedType type = (ParameterizedType) field.getGenericType();
				System.out.println(type.getActualTypeArguments()[0]);
			}

			System.out.println(float.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
