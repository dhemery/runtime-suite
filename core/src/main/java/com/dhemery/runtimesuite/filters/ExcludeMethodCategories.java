package com.dhemery.runtimesuite.filters;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.dhemery.runtimesuite.MethodFilter;
import com.dhemery.runtimesuite.internal.MethodInspector;

/**
 * A filter that rejects each method if it is in a disallowed category.
 * See {@link Category} for details of how to place classes in categories.
 * @author Dale H. Emery
 */
public class ExcludeMethodCategories implements MethodFilter {
	private final Collection<Class<?>> disallowedCategories;

	/**
	 * @param disallowedCategories the list of categories rejected by this filter.
	 */
	public ExcludeMethodCategories(Class<?>...disallowedCategories) {
		this.disallowedCategories = Arrays.asList(disallowedCategories);
	}

	/**
	 * Rejects the method if it is in a disallowed category.
	 * @param candidateMethod the method to evaluate.
	 * @return {@code false} if {@code candidateMethod} is in at least one disallowed category,
	 * otherwise {@code true}.
	 */
	public boolean passes(Method candidateMethod) {
		return Collections.disjoint(disallowedCategories, MethodInspector.categoriesOn(candidateMethod));
	}
}
