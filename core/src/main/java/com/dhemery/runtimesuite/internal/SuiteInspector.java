package com.dhemery.runtimesuite.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runners.model.InitializationError;

import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.Filter;
import com.dhemery.runtimesuite.Finder;
import com.dhemery.runtimesuite.MethodFilter;
import static java.lang.String.*;

public class SuiteInspector {
	Log log = LogFactory.getLog(SuiteInspector.class);
	private final Object suite;

	public SuiteInspector(Class<?> objectClass) throws InitializationError {
		suite = instantiate(objectClass);
	}

	public List<ClassFilter> classFilters() throws InitializationError {
		List<ClassFilter> filters = membersWith(Filter.class, ClassFilter.class);
		log.debug(format("Class filters on %s are %s", suite.getClass(), filters));
		return filters;
	}

	public List<ClassFinder> classFinders() throws InitializationError {
		List<ClassFinder> finders = membersWith(Finder.class, ClassFinder.class);
		log.debug(format("Class finders on %s are %s", suite.getClass(), finders));
		return finders;
	}

	public List<MethodFilter> methodFilters() throws InitializationError {
		List<MethodFilter> filters = membersWith(Filter.class, MethodFilter.class);
		log.debug(format("Method filters on %s are %s", suite.getClass(), filters));
		return filters;
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
			return (T) field.get(suite);
		} catch (Throwable cause) {
			throw new InitializationError(cause);
		}
	}

	public <T> List<T> membersWith(Class<? extends Annotation> annotation, Class<T> type) throws InitializationError {
		List<T> result = new ArrayList<T>();
		for(Field field : suite.getClass().getFields()) {
			if(matches(field, annotation, type)) {
				result.add(this.<T>member(field));
			}
		}
		return result;
	}
}
