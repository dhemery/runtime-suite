package com.dhemery.runtimesuite.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Classpath {

	private final String classpath;

	public Classpath(String classpath) {
		this.classpath = classpath;
	}

	public Collection<Class<?>> allClasses() {
		File directory = new File(classpath);
		System.out.println("Looking for class files in " + directory);		
		if(!directory.isDirectory()) return Collections.emptyList();
		return classesInDirectory(directory);
	}
	
	private Collection<Class<?>> classesInDirectory(File directory) {
		Collection<Class<?>> classes = new ArrayList<Class<?>>();
		System.out.println("Classes in directory " + directory);
		for(File file : directory.listFiles()) {
			System.out.println("Checking file " + file);
			if(isClassFile(file)) {
				classes.add(classForFile(file));
			} else if (file.isDirectory()) {
				classes.addAll(classesInDirectory(file));
			}
		}
		return classes;
	}

	private Class<?> classForFile(File file) {
		try {
			return Class.forName(classNameForFile(file));
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	private String classNameForFile(File file) {
		return convertFilePathToPackageName(stripExtension(stripClasspath(file.getPath())));
	}

	private String convertFilePathToPackageName(String filePath) {
		return filePath.replace(File.separatorChar, '.');
	}

	private String stripExtension(String filePath) {
		int baseNameLength = filePath.length() - ".class".length();
		return filePath.substring(0, baseNameLength);
	}

	private String stripClasspath(String filePath) {
		return filePath.substring(classpath.length() + 1);
	}

	private boolean isClassFile(File file) {
		return file.isFile() && hasClassExtension(file);
	}

	private boolean hasClassExtension(File file) {
		return file.getName().endsWith(".class");
	}
}
