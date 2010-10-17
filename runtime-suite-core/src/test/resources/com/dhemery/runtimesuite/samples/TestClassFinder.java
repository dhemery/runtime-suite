package com.dhemery.runtimesuite.samples;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.dhemery.runtimesuite.ClassFinder;

public class TestClassFinder implements ClassFinder {
	private List<Class<?>> classesToFind;

	public TestClassFinder(Class<?>...classesToFind) {
		this.classesToFind = Arrays.asList(classesToFind);
	}

	public Collection<Class<?>> find() {
		return classesToFind;
	}
}
