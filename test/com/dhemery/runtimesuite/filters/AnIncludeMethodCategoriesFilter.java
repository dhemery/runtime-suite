package com.dhemery.runtimesuite.filters;

import org.junit.Test;
import static org.junit.Assert.*;



import com.dhemery.runtimesuite.MethodFilter;
import com.dhemery.runtimesuite.filters.IncludeMethodCategories;

import example.categories.CategoryA;
import example.categories.CategoryB;
import example.tests.ClassWithCategorizedMethods;


public class AnIncludeMethodCategoriesFilter {
	private Class<?> targetClass = ClassWithCategorizedMethods.class;

	@Test
	public void forAnEmptyCategoryList_passesNoMethods() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new IncludeMethodCategories();
		assertFalse(filter.passes(targetClass.getMethod("methodWithNoCategories")));
		assertFalse(filter.passes(targetClass.getMethod("methodInCategoryA")));
		assertFalse(filter.passes(targetClass.getMethod("methodInCategoryB")));
		assertFalse(filter.passes(targetClass.getMethod("methodInCategoriesAandB")));
		assertFalse(filter.passes(targetClass.getMethod("methodInCategoriesCandD")));
	}

	@Test
	public void forASingleCategory_passesEachMethodInThatCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new IncludeMethodCategories(CategoryA.class);
		assertTrue(filter.passes(targetClass.getMethod("methodInCategoryA")));
		assertTrue(filter.passes(targetClass.getMethod("methodInCategoriesAandB")));
	}

	@Test
	public void forASingleCategory_rejectsEachMethodNotInThatCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new IncludeMethodCategories(CategoryA.class);
		assertFalse(filter.passes(targetClass.getMethod("methodWithNoCategories")));
		assertFalse(filter.passes(targetClass.getMethod("methodInCategoryB")));
		assertFalse(filter.passes(targetClass.getMethod("methodInCategoriesCandD")));
	}
	
	@Test
	public void forMultipleCategories_passesEachMethodInAnySpecifiedCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new IncludeMethodCategories(CategoryA.class, CategoryB.class);
		assertTrue(filter.passes(targetClass.getMethod("methodInCategoryA")));
		assertTrue(filter.passes(targetClass.getMethod("methodInCategoryB")));
		assertTrue(filter.passes(targetClass.getMethod("methodInCategoriesAandB")));
	}
	
	@Test
	public void forMultipleCategories_rejectsEachMethodThatLacksEverySpecifiedCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new IncludeMethodCategories(CategoryA.class, CategoryB.class);
		assertFalse(filter.passes(targetClass.getMethod("methodWithNoCategories")));
		assertFalse(filter.passes(targetClass.getMethod("methodInCategoriesCandD")));
	}
}
