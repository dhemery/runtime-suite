package com.dhemery.runtimesuite.finders;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.internal.ClasspathElement;


public class ClassesOnClassPath implements ClassFinder {
	private final String classpath;

	public ClassesOnClassPath(String classpath) {
		this.classpath = classpath;
	}

	@Override
	public Collection<Class<?>> find() {
		Collection<Class<?>> classes = new ArrayList<Class<?>>();
		for(String path : classpath.split(File.pathSeparator)) {
			classes.addAll(new ClasspathElement(path).allClasses());
		}
		return classes;
	}
}
