package com.dhemery.runtimesuite.finders;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.internal.Classpath;


public class ClassesOnClasspathFred implements ClassFinder {
	private final String classpath;

	public ClassesOnClasspathFred(String classpath) {
		this.classpath = classpath;
	}

	@Override
	public Collection<Class<?>> find() {
		return testClasses(allClasses());
	}

	private Collection<Class<?>> allClasses() {
		Collection<Class<?>> classes = new ArrayList<Class<?>>();
		for(String path : classpath.split(File.pathSeparator)) {
			Classpath pathElement = new Classpath(path);
			classes.addAll(pathElement.allClasses());
		}
		return classes;
	}

	private Collection<Class<?>> testClasses(Collection<Class<?>> classes) {
		Collection<Class<?>> testClasses = new ArrayList<Class<?>>();
		for(Class<?> each : classes) {
			if(hasTestMethods(each)) {
				testClasses.add(each);
			}
		}
		return testClasses;
	}

	private boolean hasTestMethods(Class<?> clazz) {
		for(Method method : clazz.getMethods()) {
			if(method.isAnnotationPresent(Test.class)) return true;
		}
		return false;
	}
}
