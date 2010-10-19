package com.dhemery.runtimesuite.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.runners.model.InitializationError;

import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.Filter;
import com.dhemery.runtimesuite.Finder;
import com.dhemery.runtimesuite.MethodFilter;

public class SuiteInspector {
	private final Object object;

	public SuiteInspector(Class<?> objectClass) throws InitializationError {
		object = instantiate(objectClass);
	}

	public List<ClassFilter> classFilters() throws InitializationError {
		return membersWith(Filter.class, ClassFilter.class);
	}

	public List<ClassFinder> classFinders() throws InitializationError {
		return membersWith(Finder.class, ClassFinder.class);
	}

	public List<MethodFilter> methodFilters() throws InitializationError {
		return membersWith(Filter.class, MethodFilter.class);
	}

	private boolean hasAnnotation(Field field, Class<? extends Annotation> annotation) {
		return field.isAnnotationPresent(annotation);
	}

	private boolean hasType(Field field, Class<?> type) {
		return type.isAssignableFrom(field.getType());
	}

	private Object instantiate(Class<?> suiteClass) throws InitializationError {
		try {
			return suiteClass.newInstance();
		} catch (Throwable cause) {
			throw new InitializationError(cause);
		}
	}

	private <T> boolean matches(Field field, Class<? extends Annotation> annotation, Class<T> type) {
		return hasAnnotation(field, annotation) && hasType(field, type);
	}

	@SuppressWarnings("unchecked")
	private <T> T member(Field field) throws InitializationError {
		try {
			return (T) field.get(object);
		} catch (Throwable cause) {
			throw new InitializationError(cause);
		}
	}

	public <T> List<T> membersWith(Class<? extends Annotation> annotation, Class<T> type) throws InitializationError {
		List<T> result = new ArrayList<T>();
		for(Field field : object.getClass().getFields()) {
			if(matches(field, annotation, type)) {
				result.add(this.<T>member(field));
			}
		}
		return result;
	}
}
