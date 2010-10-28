package com.dhemery.runtimesuite.filters;

import java.lang.reflect.Method;

import com.dhemery.runtimesuite.internal.MethodCategoryFilter;

/**
 * A filter that rejects each method if it is in a disallowed category.
 * See {@link Category} for details of how to place classes in categories.
 * @author Dale H. Emery
 */
public class ExcludeMethodCategories extends MethodCategoryFilter {
	/**
	 * @param disallowedCategories the list of categories rejected by this filter.
	 */
	public ExcludeMethodCategories(Class<?>...disallowedCategories) {
		super(disallowedCategories);
	}

	/**
	 * Rejects the method if it is in a disallowed category.
	 * @param candidateMethod the method to evaluate.
	 * @return {@code false} if {@code candidateMethod} is in at least one disallowed category,
	 * otherwise {@code true}.
	 */
	public boolean passes(Method candidateMethod) {
		return !hasMatchingCategory(candidateMethod);
	}
}
