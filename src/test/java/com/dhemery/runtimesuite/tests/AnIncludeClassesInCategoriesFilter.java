package com.dhemery.runtimesuite.tests;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.fest.assertions.Assertions.*;

import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.IncludeClassesInCategories;

public class AnIncludeClassesInCategoriesFilter {
	public interface CategoryA {}
	public interface CategoryB {}

	public class ClassWithNoCategories {}

	@Category(CategoryA.class)
	public class ClassInCategoryA {}

	@Category(CategoryB.class)
	public class ClassInCategoryB {}
	
	@Category({CategoryA.class, CategoryB.class})
	public class ClassInCategoriesAandB {}
	
	@Test public void forASingleCategory_passesClassesInThatCategory() {
		ClassFilter filter = new IncludeClassesInCategories(CategoryA.class);
		assertThat(filter.passes(ClassInCategoryA.class)).isTrue();
		assertThat(filter.passes(ClassInCategoriesAandB.class)).isTrue();
	}
	
	@Test public void forASingleCategory_doesNotPassClassesThatLackThatCategory() {
		ClassFilter filter = new IncludeClassesInCategories(CategoryA.class);
		assertThat(filter.passes(ClassWithNoCategories.class)).isFalse();
		assertThat(filter.passes(ClassInCategoryB.class)).isFalse();
	}
}
