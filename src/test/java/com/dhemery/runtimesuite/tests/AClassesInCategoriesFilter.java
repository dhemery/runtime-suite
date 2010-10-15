package com.dhemery.runtimesuite.tests;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.fest.assertions.Assertions.*;

import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.ClassesInCategories;

public class AClassesInCategoriesFilter {
	public interface CategoryA {}
	public interface CategoryB {}
	public interface CategoryC {}
	public interface CategoryD {}

	public class ClassWithNoCategories {}

	@Category(CategoryA.class)
	public class ClassInCategoryA {}

	@Category(CategoryB.class)
	public class ClassInCategoryB {}
	
	@Category({CategoryA.class, CategoryB.class})
	public class ClassInCategoriesAandB {}
	
	@Category({CategoryC.class, CategoryD.class})
	public class ClassInCategoriesCandD {}
	
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
