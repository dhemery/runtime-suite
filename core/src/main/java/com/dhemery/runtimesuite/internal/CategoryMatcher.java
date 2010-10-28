package com.dhemery.runtimesuite.internal;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dhemery.runtimesuite.filters.Category;

public class CategoryMatcher<T extends AnnotatedElement> {
	private Log log = LogFactory.getLog(CategoryMatcher.class);
	private final Collection<Class<?>> matchingCategories;

	public CategoryMatcher(Class<?>...matchingCategories) {
		this.matchingCategories = Arrays.asList(matchingCategories);
	}

	private Collection<Class<?>> categoriesOn(T element) {
		if(!element.isAnnotationPresent(Category.class)) {
			log.debug(String.format("%s has no Category annotation", element));
			return Collections.emptyList();
		}
		Class<?>[] categories = element.getAnnotation(Category.class).value();
		log.debug(String.format("%s is in categories %s", element, categories));
		return Arrays.asList(categories);
	}

	public boolean hasMatchingCategory(T element) {
		return !Collections.disjoint(matchingCategories, categoriesOn(element));
	}
}
