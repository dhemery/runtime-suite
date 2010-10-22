package com.dhemery.runtimesuite.filters;

import java.lang.reflect.Method;

import org.junit.experimental.categories.Category;

import com.dhemery.runtimesuite.MethodFilter;

/**
 * <p>
 * A filter that rejects each method if it is in a disallowed category.
 * </p>
 * @author Dale H. Emery
 * @see Category
 */
public class MethodsNotInCategories implements MethodFilter {
	private final MethodFilter inCategories;

	/**
	 * @param disallowedCategories the list of categories rejected by this filter.
	 */
	public MethodsNotInCategories(Class<?>...disallowedCategories) {
		inCategories = new MethodsInCategories(disallowedCategories);
	}

	/**
	 * Rejects the method if it is in a disallowed category.
	 * @param candidateMethod the method to evaluate.
	 * @return {@code false} if {@code candidateMethod} is in at least one disallowed category,
	 * otherwise {@code true}.
	 */
	public boolean passes(Method candidateMethod) {
		return !inCategories.passes(candidateMethod);
	}
}
