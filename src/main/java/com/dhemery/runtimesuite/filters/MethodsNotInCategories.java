package com.dhemery.runtimesuite.filters;

import java.lang.reflect.Method;

import com.dhemery.runtimesuite.MethodFilter;

public class MethodsNotInCategories implements MethodFilter {
	private final MethodFilter inCategories;

	public MethodsNotInCategories(Class<?>...disallowedCategories) {
		inCategories = new MethodsInCategories(disallowedCategories);
	}

	public boolean passes(Method candidatemMethod) {
		return !inCategories.passes(candidatemMethod);
	}
}
