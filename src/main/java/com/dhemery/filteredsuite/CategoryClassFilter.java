package com.dhemery.filteredsuite;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.experimental.categories.Category;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;


public class CategoryClassFilter extends Filter {
	private List<Class<?>> inclusionCategories = Collections.emptyList();
	private List<Class<?>> exclusionCategories = Collections.emptyList();

	public CategoryClassFilter(Class<?> filterSpecificationClass) {
		addInclusionCategories(filterSpecificationClass);
		addExclusionCategories(filterSpecificationClass);
	}

	private void addExclusionCategories(Class<?> filterSpecificationClass) {
		if(filterSpecificationClass.isAnnotationPresent(ExcludeClassesWithCategories.class)) {
			exclusionCategories = Arrays.asList(filterSpecificationClass.getAnnotation(ExcludeClassesWithCategories.class).value());
		}
	}

	private void addInclusionCategories(Class<?> filterSpecificationClass) {
		if(filterSpecificationClass.isAnnotationPresent(IncludeClassesWithCategories.class)) {
			inclusionCategories = Arrays.asList(filterSpecificationClass.getAnnotation(IncludeClassesWithCategories.class).value());
		}
	}

	public boolean allows(Class<?> c) {
		return includes(c) && !excludes(c);
	}

	private List<Class<?>> categoriesOn(Class<?> c) {
		if(c.isAnnotationPresent(Category.class)) {
			return Arrays.asList(c.getAnnotation(Category.class).value());
		}
		return Collections.emptyList();
		
	}

	private boolean excludes(Class<?> c) {
		return hasExclusionCategories() && excludesACategoryOn(c);
	}

	private boolean excludesACategoryOn(Class<?> c) {
		return !Collections.disjoint(categoriesOn(c), exclusionCategories);
	}

	private boolean hasExclusionCategories() {
		return !exclusionCategories.isEmpty();
	}

	private boolean hasNoInclusionCategories() {
		return inclusionCategories.isEmpty();
	}

	private boolean includes(Class<?> c) {
		return hasNoInclusionCategories() || includesACategoryOn(c);
	}

	private boolean includesACategoryOn(Class<?> c) {
		return !Collections.disjoint(categoriesOn(c), inclusionCategories);
	}

	public boolean shouldRun(Description description) {
		return allows(description.getTestClass());
	}

	public String describe() {
		return "CategoryClassfilter includes " + inclusionCategories + " excludes " + exclusionCategories;
	}
}
