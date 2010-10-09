package com.dhemery.filteredsuite.tests;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

import com.dhemery.filteredsuite.CategoryClassFilter;
import com.dhemery.filteredsuite.fixtures.SuiteThatExcludesCategoryB;
import com.dhemery.filteredsuite.fixtures.SuiteThatIncludesCategoryA;
import com.dhemery.filteredsuite.fixtures.SuiteThatIncludesCategoryAandExcludesCategoryB;
import com.dhemery.filteredsuite.fixtures.SuiteWithNoInclusionsOrExclusions;
import com.dhemery.filteredsuite.fixtures.TestClassWithCategoriesAandB;
import com.dhemery.filteredsuite.fixtures.TestClassWithCategoryA;
import com.dhemery.filteredsuite.fixtures.TestClassWithCategoryB;
import com.dhemery.filteredsuite.fixtures.TestClassWithNoCategories;


public class ACategoryClassFilter {
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
