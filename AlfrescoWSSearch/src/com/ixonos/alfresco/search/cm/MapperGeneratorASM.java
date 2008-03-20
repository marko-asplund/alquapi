package com.ixonos.alfresco.search.cm;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.*;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.util.CheckClassAdapter;

import com.ixonos.alfresco.search.annotation.AnnotationUtils;
import com.ixonos.alfresco.search.annotation.Property;



public class MapperGeneratorASM {
	private static final Logger logger = Logger.getLogger(MapperGeneratorASM.class);
	private static final int VAR_INDEX_CONTENT_OBJECT = 0;
	private static final int VAR_INDEX_PROPERTY_MAP = 1;
	@SuppressWarnings("unchecked")
	private static Map<String, Class> mapperRegistry = new HashMap<String, Class>();
	private static final PropertySetCallGenerator propertySetCallGenerator = new PropertySetCallGenerator();
	private static final boolean debug = false;

	
	@SuppressWarnings("unchecked")
	public synchronized Class generateMapper(Object contentObject, Class superClass) {
		if(mapperRegistry.containsKey(contentObject.getClass().getCanonicalName()))
			return mapperRegistry.get(contentObject.getClass().getCanonicalName());
		String className = contentObject.getClass().getCanonicalName() + "Mapper";
		logger.debug("generating mapper: "+className+": "+superClass);

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS
				+ ClassWriter.COMPUTE_FRAMES);
		cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, className.replace('.', '/'),
				null, Type.getInternalName(superClass), null);

		generateConstructor(cw, superClass);
		generateSetContentProperties(cw, contentObject, superClass);
		
		cw.visitEnd();
		
		byte[] classCode = cw.toByteArray();
		if(debug) dumpToFile(className, classCode);
		CheckClassAdapter.verify(new ClassReader(classCode), false, new PrintWriter(System.out));
		
		Class mapper = defineClass(classCode, className);
		mapperRegistry.put(contentObject.getClass().getCanonicalName(), mapper);
		logger.debug("mapper generated: "+mapper.getCanonicalName());
		
		return mapper;
	}
	
	@SuppressWarnings("unused")
	private void dumpToFile(String className, byte[] classCode) {
		try {
			String fn = System.getProperty("user.home")+"/"+className.replace('.', '_')+".class";
			FileOutputStream o = new FileOutputStream(fn);
			logger.debug("bytecode dumped to "+fn);
			o.write(classCode);
			o.close();
		} catch (Exception e) {
			logger.error("dump failed", e);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private Class defineClass(byte[] classCode, String className) {
		Class clazz = null;
		try {
			ClassLoader loader = getClassLoader();
			Class cls = Class.forName("java.lang.ClassLoader");
			java.lang.reflect.Method method =
				cls.getDeclaredMethod("defineClass", new Class[] {
						String.class, ByteBuffer.class, ProtectionDomain.class });
			method.setAccessible(true);
			try {
				Object[] args = new Object[] { className, ByteBuffer.wrap(classCode), null};
				clazz = (Class) method.invoke(loader, args);
			} finally {
				method.setAccessible(false);
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to load generated class", e);
		}
		return clazz;
	}
	
	private ClassLoader getClassLoader() {
		return getClass().getClassLoader();
	}

	@SuppressWarnings("unchecked")
	private void generateConstructor(ClassWriter cw, Class superClass) {
		Method m = Method.getMethod("void <init> ()");
		GeneratorAdapter mg = new GeneratorAdapter(ACC_PUBLIC, m, null, null, cw);
		mg.loadThis();
		mg.invokeConstructor(Type.getType(superClass), m);
		mg.returnValue();
		mg.endMethod();
	}
	
	@SuppressWarnings("unchecked")
	private void generateSetContentProperties(ClassWriter cw, Object contentObject, Class superClass) {
		Method m = Method.getMethod("void setContentProperties("+Content.class.getCanonicalName()+
				", "+ContentResultAdapter.class.getCanonicalName()+")");
		GeneratorAdapter mg = new GeneratorAdapter(ACC_PUBLIC, m, null, null, cw);
		if(debug) mg.visitInsn(Opcodes.NOP);
		
		// call superclass mapper
		Boolean useParent = (Boolean)AnnotationUtils.getContentAttributeValue(contentObject, "useParentMapper");
		if(useParent) {
			mg.loadThis();
			mg.loadArgs();
			mg.invokeConstructor(Type.getType(superClass), m);
			if(debug) mg.visitInsn(Opcodes.NOP);
		}
		
		// check cast:
		// DemoContent c = (DemoContent)content;
		mg.loadArg(VAR_INDEX_CONTENT_OBJECT);
		Type contentType = Type.getType(contentObject.getClass());
		mg.checkCast(contentType);
		int cTypeIdx = mg.newLocal(contentType);
		mg.storeLocal(cTypeIdx);
		if(debug) mg.visitInsn(Opcodes.NOP);

		// store content properties from result adapter in a local variable:
		// Map<String, String> p = result.getProperties();
		mg.loadArg(VAR_INDEX_PROPERTY_MAP);
		mg.invokeVirtual(Type.getType(ContentResultAdapter.class), Method.getMethod("java.util.Map getProperties()"));
		int propMapId = mg.newLocal(Type.getType(Map.class));
		mg.storeLocal(propMapId);
		if(debug) mg.visitInsn(Opcodes.NOP);
		
		SetterGenerationContext ctx = new SetterGenerationContext(mg, propMapId, cTypeIdx, contentObject.getClass());
		Field[] fields = contentObject.getClass().getDeclaredFields();
		for(Field f : fields)
			if(f.isAnnotationPresent(Property.class)) {
				Annotation a = f.getAnnotation(Property.class);
				String fieldName = f.getName();
				String propertyName = null;
				if(f.getType().isPrimitive())
					throw new RuntimeException("Primitive types not supported");
				try {
					propertyName = (String)a.getClass().getMethod("qName").invoke(a, new Object[]{});
					ctx.setFieldContext(f, propertyName);
					Object cg = PropertySetCallGenerator.class.getMethod("getSetCallGenerator", f.getType(),
							SetterGenerationContext.class).invoke(propertySetCallGenerator,
							new Object[] { null, ctx });
					((PropertySetCallGenerator)cg).generate();
				} catch (NoSuchMethodException e) {
					throw new RuntimeException("Type not supported: "+f.getType(), e);
				} catch (Exception e) {
					throw new RuntimeException("Mapper generation failed", e);
				}
				logger.trace("field: "+fieldName+", "+f.getType()+", "+propertyName);
			}
		
		mg.returnValue();
		mg.endMethod();
	}
	
}
