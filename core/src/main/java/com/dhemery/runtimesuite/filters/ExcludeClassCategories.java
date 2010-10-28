package com.dhemery.runtimesuite.filters;

import com.dhemery.runtimesuite.internal.ClassCategoryFilter;

/**
 * A filter that rejects each class if it is in a disallowed category.
 * See {@link Category} for details of how to place classes in categories.
 * @author Dale H. Emery
 */
public class ExcludeClassCategories extends ClassCategoryFilter {
	/**
	 * @param disallowedCategories the list of categories rejected by this filter.
	 */
	public ExcludeClassCategories(Class<?>...disallowedCategories) {
		super(disallowedCategories);
	}

	/**
	 * Rejects the class if it is in a disallowed category.
	 * @param candidateClass the class to evaluate.
	 * @return {@code false} if {@code #candidateClass} is in a disallowed category,
	 * otherwise {@code true}. 
	 */
	public boolean passes(Class<?> candidateClass) {
		return !hasMatchingCategory(candidateClass);
	}
}
