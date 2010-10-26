package com.dhemery.runtimesuite.filters;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dhemery.runtimesuite.ClassFilter;

/**
 * A filter that accepts each class if it is in an allowed category.
 * See {@link Category} for details of how to place classes in categories.
 * @author Dale H. Emery
 */
public class IncludeClassCategories implements ClassFilter {
	Log log = LogFactory.getLog(IncludeClassCategories.class);
	private final List<Class<?>> allowedCategories;

	/**
	 * @param allowedCategories the list of categories allowed by this filter.
	 */
	public IncludeClassCategories(Class<?>...allowedCategories) {
		this.allowedCategories = Arrays.asList(allowedCategories);
	}

	/**
	 * Accepts the class if it is in an allowed category.
	 * @param candidateClass the class to evaluate.
	 * @return {@code true} if {@code candidateClass} is in an allowed category,
	 * otherwise {@code false}. 
	 */
	public boolean passes(Class<?> candidateClass) {
		return !Collections.disjoint(allowedCategories, categoriesOn(candidateClass));
	}

	private Collection<Class<?>> categoriesOn(Class<?> candidateClass) {
		if(!candidateClass.isAnnotationPresent(Category.class)) {
			log.debug(String.format("Class %s has no Category annotation", candidateClass));
			return Collections.emptyList();
		}
		Class<?>[] categories = candidateClass.getAnnotation(Category.class).value();
		log.debug(String.format("Class %s is in categories %s", candidateClass, categories));
		return Arrays.asList(categories);
	}
}
