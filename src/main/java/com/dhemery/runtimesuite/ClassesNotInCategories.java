package com.dhemery.runtimesuite;

public class ClassesNotInCategories implements ClassFilter {
	private final ClassFilter inCategories;

	public ClassesNotInCategories(Class<?>...disallowedCategories) {
		inCategories = new ClassesInCategories(disallowedCategories);
	}

	public boolean passes(Class<?> candidateClass) {
		return !inCategories.passes(candidateClass);
	}
}
