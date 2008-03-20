package com.ixonos.alfresco.search.cm;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;

/**
 * Generates bytecode for calling content model object setter for an annotated field
 * in automatically generated mapper class.
 * 
 * @author Marko Asplund
 */
class PropertySetCallGenerator {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(PropertySetCallGenerator.class);

	protected Method getSetterMethod(Field field) {
		String fieldName = field.getName();
		Method setter = Method.getMethod("void set"+ fieldName.substring(0, 1).toUpperCase()+
				fieldName.substring(1)+"("+field.getType().getCanonicalName()+")");
		return setter;
	}

	void generate() {
	}

	void generateStart(SetterGenerationContext ctx) {
		ctx.generator.visitInsn(Opcodes.NOP);
		ctx.generator.loadLocal(ctx.contentObjectVarIdx);
	}
	
	void generateEnd(SetterGenerationContext ctx) {
		ctx.generator.invokeVirtual(Type.getType(ctx.contentClass), getSetterMethod(ctx.field));
	}
	
	void generateCallGetter(SetterGenerationContext ctx) {
		// p.get("foobar")
		ctx.generator.loadLocal(ctx.propertyMapVarIdx);
		ctx.generator.visitLdcInsn(ctx.propertyName);
		ctx.generator.invokeInterface(Type.getType(Map.class), Method.getMethod("Object get(Object)"));
		ctx.generator.checkCast(Type.getType(String.class));		
	}
	
	public PropertySetCallGenerator getSetCallGenerator(String s, final SetterGenerationContext ctx) {
		return new PropertySetCallGenerator() {
			@Override
			public void generate() {
				generateStart(ctx);
				generateCallGetter(ctx);
				generateEnd(ctx);
			}
		};
	}

	public PropertySetCallGenerator getSetCallGenerator(Date d, final SetterGenerationContext ctx) {
		return new PropertySetCallGenerator() {
			@Override
			public void generate() {
				generateStart(ctx);
				generateCallGetter(ctx);
				ctx.generator.invokeStatic(Type.getType(ContentMapperBase.class), Method.getMethod("java.util.Date parseAlfrescoDate(String)"));
				generateEnd(ctx);
			}
		};
	}

	public PropertySetCallGenerator getSetCallGenerator(Integer i, final SetterGenerationContext ctx) {
		return new PropertySetCallGenerator() {
			@Override
			public void generate() {
				generateStart(ctx);
				ctx.generator.newInstance(Type.getType(Integer.class));
				ctx.generator.dup();
				generateCallGetter(ctx);
				ctx.generator.invokeConstructor(Type.getType(Integer.class), Method.getMethod("void <init> (String)"));
				generateEnd(ctx);
			}
		};
	}
}
