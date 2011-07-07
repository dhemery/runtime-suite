package com.dhemery.runtimesuite;

import java.util.Collection;

/**
 * Finds test classes for possible inclusion in a runtime suite.
 * Implementing this interface allows the implementor to act as a test class finder in a runtime suite.
 * @author Dale H. Emery
 */

public interface ClassFinder {
	/**
	 * Finds test classes for possible inclusion in a runtime suite.
	 * @return a {@link Collection} of test classes for possible inclusion in a runtime suite.
	 * @see RuntimeSuite
	 */
	Collection<Class<?>> find();
}