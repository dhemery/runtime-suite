package com.dhemery.runtimesuite;

/**
 * Assesses whether a class satisfies some criteria. 
 * Implementing this interface allows the implementor to act as a test class filter
 * in a runtime suite.
 * @author Dale H. Emery
 * @see RuntimeSuite
 */
public interface ClassFilter {
	/**
	 * Indicates whether a class satisfies this filter's criteria.
	 * @param candidateClass the class to evaluate against this filter's criteria.
	 * @return {@code true} only if {@code candidateClass} satisfies this filter's criteria.
	 */
	boolean passes(Class<?> candidateClass);
}