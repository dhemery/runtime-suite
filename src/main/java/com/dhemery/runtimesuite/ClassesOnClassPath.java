package com.dhemery.runtimesuite;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;


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
		String fileNameWithinClasspath = file.getPath().substring(classpath.length() + 1);
		int fileNameLength = fileNameWithinClasspath.length();
		String fileNameWithClassSuffixStripped = fileNameWithinClasspath.substring(0, fileNameLength - ".class".length());
		String className = fileNameWithClassSuffixStripped.replace('/', '.');
		System.out.println("File name: " + className);
		return className;
	}

	private boolean isClassFile(File file) {
		return file.isFile();
	}
}
