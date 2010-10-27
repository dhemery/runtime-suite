package com.dhemery.runtimesuite.filters;

import java.util.Arrays;
import java.util.List;

import com.dhemery.runtimesuite.ClassFilter;

/**
 * A filter that excludes specifically named classed.
 * @author Dale H. Emery
 */
public class ExcludeClasses implements ClassFilter {
	private List<Class<?>> disallowedClasses;

	/**
	 * @param disallowedClasses the list of classes rejected by this filter.
	 */
	public ExcludeClasses(Class<?>...disallowedClasses) {
		this.disallowedClasses = Arrays.asList(disallowedClasses);
	}

	/**
	 * Rejects the class if it is in this filter's list of disallowed classes.
	 * @param candidateClass the class to evaluate.
	 * @return {@code false} if {@code #candidateClass} is in this filter's list of disallowed classes,
	 * otherwise {@code true}. 
	 */
	public boolean passes(Class<?> candidateClass) {
		return !disallowedClasses.contains(candidateClass);
	}
}