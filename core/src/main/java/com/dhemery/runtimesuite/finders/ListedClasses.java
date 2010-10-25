package com.dhemery.runtimesuite.finders;

import java.util.Arrays;
import java.util.Collection;

import com.dhemery.runtimesuite.ClassFinder;

/**
 * <p>
 * A finder that "finds" the classes passed to its constructor.
 * </p>
 * @author Dale H. Emery
 */
public class ListedClasses implements ClassFinder {
	private Collection<Class<?>> classes;

	/**
	 * @param classes the list of classes to find.
	 */
	public ListedClasses(Class<?>...classes) {
		this.classes = Arrays.asList(classes);
	}

	/**
	 * Finds all classes passed to this finder's constructor.
	 * @return a {@link Collection} that contains all classes passed to this finder's constructor.
	 */
	public Collection<Class<?>> find() {
		return classes;
	}
}