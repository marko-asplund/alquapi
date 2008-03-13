package com.ixonos.alfresco.search.annotation;

import java.lang.annotation.*;


@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {
	String qName();
}
