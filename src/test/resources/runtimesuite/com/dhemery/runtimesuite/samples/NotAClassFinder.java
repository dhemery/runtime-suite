package com.dhemery.runtimesuite.samples;

import java.util.Arrays;
import java.util.List;

public class NotAClassFinder /* does not implement ClassFinder */ {
	private List<Class<?>> classes;

	public NotAClassFinder(Class<?>...classes) {
		this.classes = Arrays.asList(classes);
	}

	public List<Class<?>> find() {
		return classes;
	}

}
