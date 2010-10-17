package com.dhemery.runtimesuite.filters;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.fest.assertions.Assertions.*;

import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.filters.ClassesNotInCategories;

public class AClassesNotInCategoriesFilter {
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
