package com.dhemery.runtimesuite.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dhemery.runtimesuite.ClassFilter;

import static java.lang.String.*;

/**
 * Finds classes in a directory and its subdirectories.
 * This class is not intended for public use.
 * @author Dale H. Emery
 */
public class Classpath {
	private final Logger log = LoggerFactory.getLogger(Classpath.class);
	private final String classpath;

	public Classpath(String classpath) {
		this.classpath = classpath;
	}

	public Collection<Class<?>> classes(ClassFilter filter) {
		File directory = new File(classpath);
		if(!directory.isDirectory()) return Collections.emptyList();
		return classesInDirectory(directory, filter);
	}
	
	private Collection<Class<?>> classesInDirectory(File directory, ClassFilter filter) {
		log.debug(format("Gathering classes from %s", classpath));
		Collection<Class<?>> classes = new ArrayList<Class<?>>();
		for(File file : directory.listFiles()) {
			if(isClassFile(file)) {
				try {
					Class<?> c = classForFile(file);
					if(filter.passes(c))  {
						log.trace(format("Gathered class %s", c));
						classes.add(c);
					} else {
						log.trace(format("Rejected class %s", c));
					}
				} catch (ClassNotFoundException e) {
					log.warn(format("Unable to load class from file %s", file));
				}
			} else if (file.isDirectory()) {
				classes.addAll(classesInDirectory(file, filter));
			}
		}
		return classes;
	}

	private Class<?> classForFile(File file) throws ClassNotFoundException {
		return Class.forName(classNameForFile(file));
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
