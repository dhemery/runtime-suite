package com.dhemery.runtimesuite.internal;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.filters.Category;

public abstract class ClassCategoryFilter implements ClassFilter {
	private Log log = LogFactory.getLog(ClassCategoryFilter.class);
	private final Collection<Class<?>> filterCategories;

	public ClassCategoryFilter(Class<?>...filterCategories) {
		this.filterCategories = Arrays.asList(filterCategories);
	}

	private Collection<Class<?>> categoriesOn(Class<?> c) {
		if(!c.isAnnotationPresent(Category.class)) {
			log.debug(String.format("Class %s has no Category annotation", c));
			return Collections.emptyList();
		}
		Class<?>[] categories = c.getAnnotation(Category.class).value();
		log.debug(String.format("Class %s is in categories %s", c, categories));
		return Arrays.asList(categories);
	}

	protected boolean hasMatchingCategory(Class<?> c) {
		return !Collections.disjoint(filterCategories, categoriesOn(c));
	}
}
