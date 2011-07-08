package com.dhemery.runtimesuite.tests;

import org.junit.Test;
import static org.junit.Assert.*;



import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.filters.ExcludeClassCategories;

import example.categories.CategoryA;
import example.categories.CategoryB;
import example.tests.ClassInCategoriesAandB;
import example.tests.ClassInCategoriesCandD;
import example.tests.ClassInCategoryA;
import example.tests.ClassInCategoryB;
import example.tests.ClassWithNoCategories;


public class AnExcludeClassCategoriesFilter {
	@Test
	public void withAnEmptyCategoryList_passesAllClasses() {
		ClassFilter filter = new ExcludeClassCategories();
		assertTrue(filter.passes(ClassWithNoCategories.class));
		assertTrue(filter.passes(ClassInCategoryA.class));
		assertTrue(filter.passes(ClassInCategoryB.class));
		assertTrue(filter.passes(ClassInCategoriesAandB.class));
	}

	@Test
	public void forASingleCategory_passesEachClassThatLacksThatCategory() {
		ClassFilter filter = new ExcludeClassCategories(CategoryA.class);
		assertTrue(filter.passes(ClassWithNoCategories.class));
		assertTrue(filter.passes(ClassInCategoryB.class));
		assertTrue(filter.passes(ClassInCategoriesCandD.class));
	}
	
	@Test
	public void forASingleCategory_rejectsEachClassInThatCategory() {
		ClassFilter filter = new ExcludeClassCategories(CategoryA.class);
		assertFalse(filter.passes(ClassInCategoryA.class));
		assertFalse(filter.passes(ClassInCategoriesAandB.class));
	}
	
	@Test
	public void forMultipleCategories_passesEachClassThatLacksEverySpecifiedCategory() {
		ClassFilter filter = new ExcludeClassCategories(CategoryA.class, CategoryB.class);
		assertTrue(filter.passes(ClassWithNoCategories.class));
		assertTrue(filter.passes(ClassInCategoriesCandD.class));
	}

	@Test
	public void forMultipleCategories_rejectsEachClassInAnySpecifiedCategory() {
		ClassFilter filter = new ExcludeClassCategories(CategoryA.class, CategoryB.class);
		assertFalse(filter.passes(ClassInCategoryA.class));
		assertFalse(filter.passes(ClassInCategoryB.class));
		assertFalse(filter.passes(ClassInCategoriesAandB.class));
	}	
}
