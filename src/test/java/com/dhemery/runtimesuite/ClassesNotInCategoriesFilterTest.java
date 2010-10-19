package com.dhemery.runtimesuite;

import org.junit.Test;

import static org.fest.assertions.Assertions.*;

import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.examples.CategoryA;
import com.dhemery.runtimesuite.examples.CategoryB;
import com.dhemery.runtimesuite.examples.ClassInCategoriesAandB;
import com.dhemery.runtimesuite.examples.ClassInCategoriesCandD;
import com.dhemery.runtimesuite.examples.ClassInCategoryA;
import com.dhemery.runtimesuite.examples.ClassInCategoryB;
import com.dhemery.runtimesuite.examples.ClassWithNoCategories;
import com.dhemery.runtimesuite.filters.ClassesNotInCategories;

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
