package com.dhemery.runtimesuite.filters;

import static org.junit.Assert.*;
import org.junit.Test;



import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.filters.IncludeClassCategories;

import example.categories.CategoryA;
import example.categories.CategoryB;
import example.tests.ClassInCategoriesAandB;
import example.tests.ClassInCategoriesCandD;
import example.tests.ClassInCategoryA;
import example.tests.ClassInCategoryB;
import example.tests.ClassWithNoCategories;


public class AnIncludeClassCategoriesFilter {
	@Test
	public void withAnEmptyCategoryList_passesNoClasses() {
		ClassFilter filter = new IncludeClassCategories();
		assertFalse(filter.passes(ClassWithNoCategories.class));
		assertFalse(filter.passes(ClassInCategoryA.class));
		assertFalse(filter.passes(ClassInCategoryB.class));
		assertFalse(filter.passes(ClassInCategoriesAandB.class));
	}

	@Test
	public void forASingleCategory_passesEachClassInThatCategory() {
		ClassFilter filter = new IncludeClassCategories(CategoryA.class);
		assertTrue(filter.passes(ClassInCategoryA.class));
		assertTrue(filter.passes(ClassInCategoriesAandB.class));
	} 
	
	@Test
	public void forASingleCategory_rejectsEachClassNotInThatCategory() {
		ClassFilter filter = new IncludeClassCategories(CategoryA.class);
		assertFalse(filter.passes(ClassWithNoCategories.class));
		assertFalse(filter.passes(ClassInCategoryB.class));
	}
	
	@Test
	public void forMultipleCategories_passesEachClassInAnySpecifiedCategory() {
		ClassFilter filter = new IncludeClassCategories(CategoryA.class, CategoryB.class);
		assertTrue(filter.passes(ClassInCategoryA.class));
		assertTrue(filter.passes(ClassInCategoryB.class));
		assertTrue(filter.passes(ClassInCategoriesAandB.class));
	}
	
	@Test
	public void forMultipleCategories_rejectsEachClassThatLacksEverySpecifiedCategory() {
		ClassFilter filter = new IncludeClassCategories(CategoryA.class, CategoryB.class);
		assertFalse(filter.passes(ClassWithNoCategories.class));
		assertFalse(filter.passes(ClassInCategoriesCandD.class));
	}
}
