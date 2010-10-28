package com.dhemery.runtimesuite.filters;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.dhemery.runtimesuite.MethodFilter;
import com.dhemery.runtimesuite.internal.MethodInspector;

/**
 * A filter that accepts each method if it is in an allowed category.
 * See {@link Category} for details of how to place classes in categories.
 * @author Dale H. Emery
 */
public class IncludeMethodCategories implements MethodFilter {

	private final List<Class<?>> allowedCategories;

	/**
	 * @param allowedCategories the list of categories allowed by this filter.
	 */
	public IncludeMethodCategories(Class<?>...allowedCategories) {
		this.allowedCategories = Arrays.asList(allowedCategories);
	}

	/**
	 * Accepts a method if it is in an allowed category.
	 * @param candidateMethod the method to evaluate.
	 * @return {@code true} if {@code candidateMethod} is in one or more allowed categories,
	 * otherwise {@code false}. 
	 */
	public boolean passes(Method candidateMethod) {
		return !Collections.disjoint(allowedCategories, MethodInspector.categoriesOn(candidateMethod));
	}
}
