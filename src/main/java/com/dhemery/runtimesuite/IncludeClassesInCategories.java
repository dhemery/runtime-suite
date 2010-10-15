package com.dhemery.runtimesuite;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.experimental.categories.Category;


public class IncludeClassesInCategories implements ClassFilter {
	private final Class<?> allowedCategories;

	public IncludeClassesInCategories(Class<?> allowedCategories) {
		this.allowedCategories = allowedCategories;
	}

	public boolean passes(Class<?> candidateClass) {
		return categoriesOn(candidateClass).contains(allowedCategories);
	}

	private Collection<Class<?>> categoriesOn(Class<?> candidateClass) {
		if(!candidateClass.isAnnotationPresent(Category.class)) {
			return Collections.emptyList();
		}
		return Arrays.asList(candidateClass.getAnnotation(Category.class).value());
	}
}
