package com.dhemery.runtimesuite;

import org.junit.Test;

import static org.fest.assertions.Assertions.*;

import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.filters.ClassesNotInCategories;

import examples.CategoryA;
import examples.CategoryB;
import examples.ClassInCategoriesAandB;
import examples.ClassInCategoriesCandD;
import examples.ClassInCategoryA;
import examples.ClassInCategoryB;
import examples.ClassWithNoCategories;

public class ClassesNotInCategoriesFilterTest {
	@Test public void forASingleCategory_passesEachClassThatLacksThatCategory() {
		ClassFilter filter = new ClassesNotInCategories(CategoryA.class);
		assertThat(filter.passes(ClassWithNoCategories.class)).isTrue();
		assertThat(filter.passes(ClassInCategoryB.class)).isTrue();
		assertThat(filter.passes(ClassInCategoriesCandD.class)).isTrue();
	}
	
	@Test public void forASingleCategory_rejectsEachClassInThatCategory() {
		ClassFilter filter = new ClassesNotInCategories(CategoryA.class);
		assertThat(filter.passes(ClassInCategoryA.class)).isFalse();
		assertThat(filter.passes(ClassInCategoriesAandB.class)).isFalse();
	}
	
	@Test public void forMultipleCategories_passesEachClassThatLacksEverySpecifiedCategory() {
		ClassFilter filter = new ClassesNotInCategories(CategoryA.class, CategoryB.class);
		assertThat(filter.passes(ClassWithNoCategories.class)).isTrue();
		assertThat(filter.passes(ClassInCategoriesCandD.class)).isTrue();
	}

	@Test public void forMultipleCategories_rejectsEachClassInAnySpecifiedCategory() {
		ClassFilter filter = new ClassesNotInCategories(CategoryA.class, CategoryB.class);
		assertThat(filter.passes(ClassInCategoryA.class)).isFalse();
		assertThat(filter.passes(ClassInCategoryB.class)).isFalse();
		assertThat(filter.passes(ClassInCategoriesAandB.class)).isFalse();
	}	
}
