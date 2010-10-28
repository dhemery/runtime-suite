package com.dhemery.runtimesuite.internal;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dhemery.runtimesuite.MethodFilter;
import com.dhemery.runtimesuite.filters.Category;

public abstract class MethodCategoryFilter implements MethodFilter {
	private Log log = LogFactory.getLog(ClassCategoryFilter.class);
	private final Collection<Class<?>> filterCategories;

	public MethodCategoryFilter(Class<?>...filterCategories) {
		this.filterCategories = Arrays.asList(filterCategories);
	}

	private Collection<Class<?>> categoriesOn(Method method) {
		if(!method.isAnnotationPresent(Category.class)) {
			log.debug(String.format("%s has no Category annotation", method));
			return Collections.emptyList();
		}
		Class<?>[] categories = method.getAnnotation(Category.class).value();
		log.debug(String.format("%s is in categories %s", method, categories));
		return Arrays.asList(categories);
	}

	protected boolean hasMatchingCategory(Method method) {
		return !Collections.disjoint(filterCategories, categoriesOn(method));
	}
}
