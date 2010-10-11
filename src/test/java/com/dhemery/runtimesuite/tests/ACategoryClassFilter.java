package com.dhemery.runtimesuite.tests;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.dhemery.runtimesuite.CategoryClassFilter;
import com.dhemery.runtimesuite.ExcludeClassesWithCategories;
import com.dhemery.runtimesuite.IncludeClassesWithCategories;

public class ACategoryClassFilter {
	public class CategoryA {}
	public class CategoryB {}

	public class TestClassWithNoCategories {}
	
	@Category({CategoryA.class, CategoryB.class})
	public class TestClassWithCategoriesAandB {}

	@Category(CategoryA.class)
	public class TestClassWithCategoryA {}

	@Category(CategoryB.class)
	public class TestClassWithCategoryB {}

	public class SuiteWithNoInclusionsOrExclusions {}

	@IncludeClassesWithCategories(CategoryA.class)
	public class SuiteThatIncludesCategoryA {}

	@ExcludeClassesWithCategories(CategoryB.class)
	public class SuiteThatExcludesCategoryB {}

	@IncludeClassesWithCategories(CategoryA.class)
	@ExcludeClassesWithCategories(CategoryB.class)
	public class SuiteThatIncludesCategoryAandExcludesCategoryB {}

	@Test public void withNoInclusionsOrExclusions_allowsAllClasses() {
		CategoryClassFilter filter = new CategoryClassFilter(SuiteWithNoInclusionsOrExclusions.class);
		assertThat(filter.allows(TestClassWithNoCategories.class)).isTrue();
		assertThat(filter.allows(TestClassWithCategoryA.class)).isTrue();
		assertThat(filter.allows(TestClassWithCategoryB.class)).isTrue();
		assertThat(filter.allows(TestClassWithCategoriesAandB.class)).isTrue();
	}

	@Test public void withOnlyInclusions_allowsClassesThatHaveAnIncludedCategory() {
		CategoryClassFilter filter = new CategoryClassFilter(SuiteThatIncludesCategoryA.class);
		assertThat(filter.allows(TestClassWithCategoryA.class)).isTrue();
		assertThat(filter.allows(TestClassWithCategoriesAandB.class)).isTrue();
	}

	@Test public void withOnlyInclusions_doesNotAllowClassesThatHaveNoIncludedCategories() {
		CategoryClassFilter filter = new CategoryClassFilter(SuiteThatIncludesCategoryA.class);
		assertThat(filter.allows(TestClassWithNoCategories.class)).isFalse();
		assertThat(filter.allows(TestClassWithCategoryB.class)).isFalse();
	}

	@Test public void withOnlyExclusions_allowsClassesThatHaveNoExcludedCategories() {
		CategoryClassFilter filter = new CategoryClassFilter(SuiteThatExcludesCategoryB.class);
		assertThat(filter.allows(TestClassWithNoCategories.class)).isTrue();
		assertThat(filter.allows(TestClassWithCategoryA.class)).isTrue();
	}

	@Test public void withOnlyExclusions_doesNotAllowClassesThatHaveAnExcludedCategory() {
		CategoryClassFilter filter = new CategoryClassFilter(SuiteThatExcludesCategoryB.class);
		assertThat(filter.allows(TestClassWithCategoryB.class)).isFalse();
		assertThat(filter.allows(TestClassWithCategoriesAandB.class)).isFalse();
	}

	@Test public void withInclusionsAndExclusions_allowsClassesWithIncludedCategoriesAndNoExcludedCategories() {
		CategoryClassFilter filter = new CategoryClassFilter(SuiteThatIncludesCategoryAandExcludesCategoryB.class);
		assertThat(filter.allows(TestClassWithCategoryA.class)).isTrue();
	}

	@Test public void withInclusionsAndExclusions_doesNotAllowClassesWithBothIncludedAndExcludedCategories() {
		CategoryClassFilter filter = new CategoryClassFilter(SuiteThatIncludesCategoryAandExcludesCategoryB.class);
		assertThat(filter.allows(TestClassWithCategoriesAandB.class)).isFalse();
	}

}
