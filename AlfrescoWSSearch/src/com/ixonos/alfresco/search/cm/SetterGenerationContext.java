package com.ixonos.alfresco.search.cm;

import java.lang.reflect.Field;

import org.objectweb.asm.commons.GeneratorAdapter;

/**
 * Combines parameters required in generating setter call bytecode in PropertySetCallGenerator.
 * 
 * @author Marko Asplund
 */
@SuppressWarnings({"unused", "unchecked"})
class SetterGenerationContext {
	GeneratorAdapter generator;
	Field field;
	String propertyName;
	int propertyMapVarIdx;
	int contentObjectVarIdx;
	Class contentClass;

	SetterGenerationContext(GeneratorAdapter generator,
			int propertyMapVarIdx, int contentObjectVarIdx,
			Class contentClass) {
		this.generator = generator;
		this.propertyMapVarIdx = propertyMapVarIdx;
		this.contentObjectVarIdx = contentObjectVarIdx;
		this.contentClass = contentClass;
	}

	void setFieldContext(Field field, String propertyName) {
		this.field = field;
		this.propertyName = propertyName;
	}
}
