package com.dhemery.runtimesuite.filters;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.experimental.categories.Category;

import com.dhemery.runtimesuite.MethodFilter;

public class MethodsInCategories implements MethodFilter {

	private final List<Class<?>> allowedCategories;

	public MethodsInCategories(Class<?>...allowedCategories) {
		this.allowedCategories = Arrays.asList(allowedCategories);
	}

	public boolean passes(Method candidateMethod) {
		return !Collections.disjoint(allowedCategories, categoriesOn(candidateMethod));
	}

	private Collection<Class<?>> categoriesOn(Method candidateMethod) {
		if(!candidateMethod.isAnnotationPresent(Category.class)) {
			return Collections.emptyList();
		}
		return Arrays.asList(candidateMethod.getAnnotation(Category.class).value());
	}
}
