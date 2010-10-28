package com.dhemery.runtimesuite;

import org.junit.Test;

import static org.fest.assertions.Assertions.*;

import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.filters.IncludeClassCategories;

import examples.CategoryA;
import examples.CategoryB;
import examples.ClassInCategoriesAandB;
import examples.ClassInCategoriesCandD;
import examples.ClassInCategoryA;
import examples.ClassInCategoryB;
import examples.ClassWithNoCategories;

public class IncludeClassCategoriesTest {
	@Test
	public void withAnEmptyCategoryList_passesNoClasses() {
		ClassFilter filter = new IncludeClassCategories();
		assertThat(filter.passes(ClassWithNoCategories.class)).isFalse();
		assertThat(filter.passes(ClassInCategoryA.class)).isFalse();
		assertThat(filter.passes(ClassInCategoryB.class)).isFalse();
		assertThat(filter.passes(ClassInCategoriesAandB.class)).isFalse();
	}

	@Test
	public void forASingleCategory_passesEachClassInThatCategory() {
		ClassFilter filter = new IncludeClassCategories(CategoryA.class);
		assertThat(filter.passes(ClassInCategoryA.class)).isTrue();
		assertThat(filter.passes(ClassInCategoriesAandB.class)).isTrue();
	} 
	
	@Test
	public void forASingleCategory_rejectsEachClassNotInThatCategory() {
		ClassFilter filter = new IncludeClassCategories(CategoryA.class);
		assertThat(filter.passes(ClassWithNoCategories.class)).isFalse();
		assertThat(filter.passes(ClassInCategoryB.class)).isFalse();
	}
	
	@Test
	public void forMultipleCategories_passesEachClassInAnySpecifiedCategory() {
		ClassFilter filter = new IncludeClassCategories(CategoryA.class, CategoryB.class);
		assertThat(filter.passes(ClassInCategoryA.class)).isTrue();
		assertThat(filter.passes(ClassInCategoryB.class)).isTrue();
		assertThat(filter.passes(ClassInCategoriesAandB.class)).isTrue();
	}
	
	@Test
	public void forMultipleCategories_rejectsEachClassThatLacksEverySpecifiedCategory() {
		ClassFilter filter = new IncludeClassCategories(CategoryA.class, CategoryB.class);
		assertThat(filter.passes(ClassWithNoCategories.class)).isFalse();
		assertThat(filter.passes(ClassInCategoriesCandD.class)).isFalse();
	}
}
