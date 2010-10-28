package com.dhemery.runtimesuite.filters;

import java.lang.reflect.Method;

import com.dhemery.runtimesuite.MethodFilter;
import com.dhemery.runtimesuite.internal.CategoryMatcher;

/**
 * A filter that rejects each method if it is in a disallowed category.
 * See {@link Category} for details of how to place classes in categories.
 * @author Dale H. Emery
 */
public class ExcludeMethodCategories implements MethodFilter {
	private final CategoryMatcher<Method> matcher;

	/**
	 * @param disallowedCategories the list of categories rejected by this filter.
	 */
	public ExcludeMethodCategories(Class<?>...disallowedCategories) {
		matcher = new CategoryMatcher<Method>(disallowedCategories);
	}

	/**
	 * Rejects the method if it is in a disallowed category.
	 * @param candidateMethod the method to evaluate.
	 * @return {@code false} if {@code candidateMethod} is in at least one disallowed category,
	 * otherwise {@code true}.
	 */
	public boolean passes(Method candidateMethod) {
		return !matcher.hasMatchingCategory(candidateMethod);
	}
}
