package com.dhemery.runtimesuite.filters;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.experimental.categories.Category;

import com.dhemery.runtimesuite.ClassFilter;

/**
 * <p>
 * A filter that accepts each class if it is in an allowed category.
 * </p>
 * @author Dale H. Emery
 * @see Category
 */
public class ClassesInCategories implements ClassFilter {
	private final List<Class<?>> allowedCategories;

	/**
	 * @param allowedCategories the list of categories allowed by this filter.
	 */
	public ClassesInCategories(Class<?>...allowedCategories) {
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
			return Collections.emptyList();
		}
		return Arrays.asList(candidateClass.getAnnotation(Category.class).value());
	}
}
