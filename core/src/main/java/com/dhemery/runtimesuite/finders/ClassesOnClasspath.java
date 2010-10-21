package com.dhemery.runtimesuite.finders;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.internal.ClassesWithTestMethods;
import com.dhemery.runtimesuite.internal.Classpath;

import static java.lang.String.*;

public class ClassesOnClasspath implements ClassFinder {
	private final Log log = LogFactory.getLog(ClassesOnClasspath.class);
	private final ClassFilter withTestMethods = new ClassesWithTestMethods();
	private final String classpathList;

	public ClassesOnClasspath(String classpathList) {
		log.debug(format("Classpath string is %s", classpathList));
		this.classpathList = classpathList;
	}

	@Override
	public Collection<Class<?>> find() {
		log.trace("> find()");
		Set<Class<?>> testClasses = new HashSet<Class<?>>();
		for(String path : classpathList.split(File.pathSeparator)) {
			Classpath classpath = new Classpath(path);
			testClasses.addAll(classpath.classes(withTestMethods));
		}
		return testClasses;
	}
}
