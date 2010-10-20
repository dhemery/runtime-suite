package com.dhemery.runtimesuite;

import org.junit.Test;

import static org.fest.assertions.Assertions.*;

import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.filters.ClassesInCategories;

import examples.CategoryA;
import examples.CategoryB;
import examples.ClassInCategoriesAandB;
import examples.ClassInCategoriesCandD;
import examples.ClassInCategoryA;
import examples.ClassInCategoryB;
import examples.ClassWithNoCategories;

public class ClassesInCategoriesFilterTest {
	@Test public void forASingleCategory_passesEachClassInThatCategory() {
		ClassFilter filter = new ClassesInCategories(CategoryA.class);
		assertThat(filter.passes(ClassInCategoryA.class)).isTrue();
		assertThat(filter.passes(ClassInCategoriesAandB.class)).isTrue();
	}
	
	@Test public void forASingleCategory_rejectsEachClassNotInThatCategory() {
		ClassFilter filter = new ClassesInCategories(CategoryA.class);
		assertThat(filter.passes(ClassWithNoCategories.class)).isFalse();
		assertThat(filter.passes(ClassInCategoryB.class)).isFalse();
	}
	
	@Test public void forMultipleCategories_passesEachClassInAnySpecifiedCategory() {
		ClassFilter filter = new ClassesInCategories(CategoryA.class, CategoryB.class);
		assertThat(filter.passes(ClassInCategoryA.class)).isTrue();
		assertThat(filter.passes(ClassInCategoryB.class)).isTrue();
		assertThat(filter.passes(ClassInCategoriesAandB.class)).isTrue();
	}
	
	@Test public void forMultipleCategories_rejectsEachClassThatLacksEverySpecifiedCategory() {
		ClassFilter filter = new ClassesInCategories(CategoryA.class, CategoryB.class);
		assertThat(filter.passes(ClassWithNoCategories.class)).isFalse();
		assertThat(filter.passes(ClassInCategoriesCandD.class)).isFalse();
	}
}
