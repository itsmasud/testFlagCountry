package com.fieldnation.json;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface JsonField {

	String name() default "";

	JsonTypes jsonType() default JsonTypes.STRING;
}
