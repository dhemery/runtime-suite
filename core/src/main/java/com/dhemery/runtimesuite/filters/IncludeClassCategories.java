package com.dhemery.runtimesuite.filters;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.internal.ClassInspector;

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
		return !Collections.disjoint(allowedCategories, ClassInspector.categoriesOn(candidateClass));
	}
}
