package com.dhemery.runtimesuite.filters;

import com.dhemery.runtimesuite.ClassFilter;

public class ClassesNotInCategories implements ClassFilter {
	private final ClassFilter inCategories;

	public ClassesNotInCategories(Class<?>...disallowedCategories) {
		inCategories = new ClassesInCategories(disallowedCategories);
	}

	public boolean passes(Class<?> candidateClass) {
		return !inCategories.passes(candidateClass);
	}
}
