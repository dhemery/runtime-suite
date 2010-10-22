package com.dhemery.runtimesuite.filters;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.experimental.categories.Category;

import com.dhemery.runtimesuite.MethodFilter;

/**
 * <p>
 * A filter that accepts each method if it is in an allowed category.
 * </p>
 * @author Dale H. Emery
 * @see Category
 */
public class MethodsInCategories implements MethodFilter {

	private final List<Class<?>> allowedCategories;

	/**
	 * @param allowedCategories the list of categories allowed by this filter.
	 */
	public MethodsInCategories(Class<?>...allowedCategories) {
		this.allowedCategories = Arrays.asList(allowedCategories);
	}

	/**
	 * Accepts a method if it is in an allowed category.
	 * @param candidateMethod the method to evaluate.
	 * @return {@code true} if {@code candidateMethod} is in one or more allowed categories,
	 * otherwise {@code false}. 
	 */
	public boolean passes(Method candidateMethod) {
		return !Collections.disjoint(allowedCategories, categoriesOn(candidateMethod));
	}

	private Collection<Class<?>> categoriesOn(Method candidateMethod) {
		if(!candidateMethod.isAnnotationPresent(Category.class)) {
			return Collections.emptyList();
		}
		return Arrays.asList(candidateMethod.getAnnotation(Category.class).value());
	}
}
