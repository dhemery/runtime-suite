package com.dhemery.runtimesuite.internal;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.dhemery.runtimesuite.filters.Category;

public class MethodInspector {
	public static Collection<Class<?>> categoriesOn(Method candidateMethod) {
		if(!candidateMethod.isAnnotationPresent(Category.class)) {
			return Collections.emptyList();
		}
		return Arrays.asList(candidateMethod.getAnnotation(Category.class).value());
	}
}
