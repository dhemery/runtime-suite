package com.dhemery.runtimesuite.tests;

import org.junit.Test;
import static org.junit.Assert.*;



import com.dhemery.runtimesuite.MethodFilter;
import com.dhemery.runtimesuite.filters.ExcludeMethodCategories;

import example.categories.CategoryA;
import example.categories.CategoryB;
import example.tests.ClassWithCategorizedMethods;


public class AnExcludeMethodCategoriesFilter {
	private Class<?> targetClass = ClassWithCategorizedMethods.class;

	@Test
	public void forAnEmptyCategoryList_passesAllMethods() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new ExcludeMethodCategories();
		assertTrue(filter.passes(targetClass.getMethod("methodWithNoCategories")));
		assertTrue(filter.passes(targetClass.getMethod("methodInCategoryA")));
		assertTrue(filter.passes(targetClass.getMethod("methodInCategoryB")));
		assertTrue(filter.passes(targetClass.getMethod("methodInCategoriesAandB")));
		assertTrue(filter.passes(targetClass.getMethod("methodInCategoriesCandD")));
	}

	@Test
	public void forASingleCategory_rejectsEachMethodInThatCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new ExcludeMethodCategories(CategoryA.class);
		assertFalse(filter.passes(targetClass.getMethod("methodInCategoryA")));
		assertFalse(filter.passes(targetClass.getMethod("methodInCategoriesAandB")));
	}

	@Test
	public void forASingleCategory_passesEachMethodNotInThatCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new ExcludeMethodCategories(CategoryA.class);
		assertTrue(filter.passes(targetClass.getMethod("methodWithNoCategories")));
		assertTrue(filter.passes(targetClass.getMethod("methodInCategoryB")));
		assertTrue(filter.passes(targetClass.getMethod("methodInCategoriesCandD")));
	}
	
	@Test
	public void forMultipleCategories_rejectsEachMethodInAnySpecifiedCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new ExcludeMethodCategories(CategoryA.class, CategoryB.class);
		assertFalse(filter.passes(targetClass.getMethod("methodInCategoryA")));
		assertFalse(filter.passes(targetClass.getMethod("methodInCategoryB")));
		assertFalse(filter.passes(targetClass.getMethod("methodInCategoriesAandB")));
	}
	
	@Test
	public void forMultipleCategories_passesEachMethodThatLacksEverySpecifiedCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new ExcludeMethodCategories(CategoryA.class, CategoryB.class);
		assertTrue(filter.passes(targetClass.getMethod("methodWithNoCategories")));
		assertTrue(filter.passes(targetClass.getMethod("methodInCategoriesCandD")));
	}
}
