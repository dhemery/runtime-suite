package com.dhemery.runtimesuite;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.experimental.categories.Category;

public class ClassesInCategories implements ClassFilter {
	private final List<Class<?>> allowedCategories;

	public ClassesInCategories(Class<?>...allowedCategories) {
		this.allowedCategories = Arrays.asList(allowedCategories);
	}

	public boolean passes(Class<?> candidateClass) {
		return !Collections.disjoint(allowedCategories, categoriesOn(candidateClass));
	}

	private Collection<Class<?>> categoriesOn(Class<?> candidateClass) {
		if(!candidateClass.isAnnotationPresent(Category.class)) {
			return Collections.emptyList();
		}
		return Arrays.asList(candidateClass.getAnnotation(Category.class).value());
	}
}
