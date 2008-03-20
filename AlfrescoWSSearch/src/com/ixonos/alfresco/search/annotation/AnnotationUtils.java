package com.ixonos.alfresco.search.annotation;

import java.lang.annotation.Annotation;

@SuppressWarnings("unchecked")
public class AnnotationUtils {
	private static final Class ANN_TYPE_CONTENT = com.ixonos.alfresco.search.annotation.Content.class;
	private static final Class ANN_TYPE_PROPERTY = com.ixonos.alfresco.search.annotation.Property.class;
	
	private static Annotation getAnnotation(Object obj, Class annotationType) {
		Annotation a = obj.getClass().getAnnotation(annotationType);
		return a;
	}
	
	private static Object getAttributeValue(Object obj, Class annotationType, String attribute) {
		Annotation a = getAnnotation(obj, annotationType);
		Object value = null;
		try {
			value = a.getClass().getMethod(attribute).invoke(a, new Object[]{});
		} catch (Exception e) {
			throw new RuntimeException("Failed to read annotation attribute value: "+obj, e);
		}
		return value;
	}
	
	public static Annotation getContentAnnotation(Object obj) {
		return getAnnotation(obj, ANN_TYPE_CONTENT);
	}
	
	public static Annotation getPropertyAnnotation(Object obj) {
		return getAnnotation(obj, ANN_TYPE_PROPERTY);
	}
	
	public static Object getContentAttributeValue(Object obj, String attribute) {
		return getAttributeValue(obj, ANN_TYPE_CONTENT, attribute);
	}

	public static Object getPropertyAttributeValue(Object obj, String attribute) {
		return getAttributeValue(obj, ANN_TYPE_PROPERTY, attribute);
	}
	
}
