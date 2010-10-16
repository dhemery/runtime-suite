package com.dhemery.runtimesuite;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class ClassesOnClassPath implements ClassFinder {

	private final String classpath;

	public ClassesOnClassPath(String classpath) {
		this.classpath = classpath;
	}

	@Override
	public Collection<Class<?>> find() {
		return classesInDirectory(new File(classpath));
	}

	private Collection<Class<?>> classesInDirectory(File directory) {
		Collection<Class<?>> found = new ArrayList<Class<?>>();
		for(File file : directory.listFiles()) {
			if(isClassFile(file)) {
				found.add(classForFile(file));
			} else if (file.isDirectory()) {
				found.addAll(classesInDirectory(file));
			}
		}
		return found;
	}

	private Class<?> classForFile(File file) {
		try {
			return Class.forName(classNameForFile(file));
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	private String classNameForFile(File file) {
		System.out.println("file: " + file.getAbsolutePath());
		return "a._1._a.Test_a1a_1";
	}

	private boolean isClassFile(File file) {
		return file.isFile();
	}
}
