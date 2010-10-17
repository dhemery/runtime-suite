package com.dhemery.runtimesuite.samples;


import java.util.Arrays;
import java.util.List;

import com.dhemery.runtimesuite.ClassFilter;

public class TestClassRemover implements ClassFilter {
	private List<Class<?>> classesToRemove;

	public TestClassRemover(Class<?>...classesToRemove) {
		this.classesToRemove = Arrays.asList(classesToRemove);
	}

	public boolean passes(Class<?> candidateClass) {
		return !classesToRemove.contains(candidateClass);
	}
}