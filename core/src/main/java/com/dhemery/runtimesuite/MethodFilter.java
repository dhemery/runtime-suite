package com.dhemery.runtimesuite;

import java.lang.reflect.Method;

/**
 * Assesses whether a method satisfies some criteria. 
 * Implementing this interface allows the implementor to act as a test method filter in a {@link RuntimeSuite}.
 * 
 * @author Dale H. Emery
 */

public interface MethodFilter {
	/**
	 * Indicates whether a method satisfies this filter's criteria.
	 * @param method the method to assess against this filter's criteria.
	 * @return {@code true} only if {@code method} satisfies this filter's criteria.
	 */
	boolean passes(Method method);
}
