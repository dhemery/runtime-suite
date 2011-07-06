package com.dhemery.runtimesuite.filters;

import java.util.Arrays;
import java.util.Collection;

import com.dhemery.runtimesuite.ClassFilter;

/**
 * A filter that passes only the specifically named classed.
 * @author Dale H. Emery
 */
public class IncludeClasses implements ClassFilter {
	private final Collection<Class<?>> allowedClasses;

	/**
	 * @param allowedClasses the list of classes passed by this filter.
	 */
	public IncludeClasses(Class<?>...allowedClasses) {
		this.allowedClasses = Arrays.asList(allowedClasses);
	}

	/**
	 * Accepts the class only if it is in this filter's list of allowed classes.
	 * @param candidateClass the class to evaluate.
	 * @return {@code true} if {@code #candidateClass} is in this filter's list of allowed classes,
	 * otherwise {@code false}. 
	 */
		@Override
	public boolean passes(Class<?> candidateClass) {
		return allowedClasses.contains(candidateClass);
	}

}
