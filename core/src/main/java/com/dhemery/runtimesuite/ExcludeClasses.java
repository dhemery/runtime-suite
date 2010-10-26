package com.dhemery.runtimesuite;

import java.util.Arrays;
import java.util.List;


public class ExcludeClasses implements ClassFilter {
	private List<Class<?>> classesToRemove;

	public ExcludeClasses(Class<?>...classesToRemove) {
		this.classesToRemove = Arrays.asList(classesToRemove);
	}

	public boolean passes(Class<?> candidateClass) {
		return !classesToRemove.contains(candidateClass);
	}
}