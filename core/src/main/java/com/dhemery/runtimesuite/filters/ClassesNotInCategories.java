package com.dhemery.runtimesuite.filters;

import com.dhemery.runtimesuite.ClassFilter;

/**
 * A filter that rejects each class if it is in a disallowed category.
 * See {@link Category} for details of how to place classes in categories.
 * @author Dale H. Emery
 */
public class ClassesNotInCategories implements ClassFilter {
	private final ClassFilter inCategories;

	/**
	 * @param disallowedCategories the list of categories rejected by this filter.
	 */
	public ClassesNotInCategories(Class<?>...disallowedCategories) {
		inCategories = new ClassesInCategories(disallowedCategories);
	}

	/**
	 * Rejects the class if it is in a disallowed category.
	 * @param candidateClass the class to evaluate.
	 * @return {@code false} if {@code #candidateClass} is in a disallowed category,
	 * otherwise {@code true}. 
	 */
	public boolean passes(Class<?> candidateClass) {
		return !inCategories.passes(candidateClass);
	}
}
