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

/**
 * <p>
 * A finder that finds every class in the specified directories on the classpath.
 * </p>
 * <p>
 * Note: The current implementation searches only directories, not jar files.
 * </p>
 * @author Dale H. Emery
 */
public class ClassesOnClasspath implements ClassFinder {
	private final Log log = LogFactory.getLog(ClassesOnClasspath.class);
	private final ClassFilter withTestMethods = new ClassesWithTestMethods();
	private final String classpathList;

	/**
	 * @param classpathList the list of directories to search for classes,
	 * separated by {@link File#pathSeparatorChar}.
	 * Each directory must be on the classpath.
	 */
	public ClassesOnClasspath(String classpathList) {
		log.debug(format("Classpath string is %s", classpathList));
		this.classpathList = classpathList;
	}

	/**
	 * Finds all classes in the directories listed in this finder's {@code classpathList}.
	 * @return a {@link Collection} of all classes in the directories listed in this finder's {@code classpathList}.
	 */
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
