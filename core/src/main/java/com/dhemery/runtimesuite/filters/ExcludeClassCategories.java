package com.dhemery.runtimesuite.filters;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.internal.ClassInspector;

/**
 * A filter that rejects each class if it is in a disallowed category.
 * See {@link Category} for details of how to place classes in categories.
 * @author Dale H. Emery
 */
public class ExcludeClassCategories implements ClassFilter {
	Log log = LogFactory.getLog(ExcludeClassCategories.class);
	private final List<Class<?>> disallowedCategories;

	/**
	 * @param disallowedCategories the list of categories rejected by this filter.
	 */
	public ExcludeClassCategories(Class<?>...disallowedCategories) {
		this.disallowedCategories = Arrays.asList(disallowedCategories);
	}

	/**
	 * Rejects the class if it is in a disallowed category.
	 * @param candidateClass the class to evaluate.
	 * @return {@code false} if {@code #candidateClass} is in a disallowed category,
	 * otherwise {@code true}. 
	 */
	public boolean passes(Class<?> candidateClass) {
		return Collections.disjoint(disallowedCategories, ClassInspector.categoriesOn(candidateClass));
	}
}
