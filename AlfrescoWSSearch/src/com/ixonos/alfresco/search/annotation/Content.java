package com.ixonos.alfresco.search.annotation;

import java.lang.annotation.*;


@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Content {
	String qName();
	boolean useParentMapper() default false;
}
