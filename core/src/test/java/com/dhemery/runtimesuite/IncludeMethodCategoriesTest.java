package com.dhemery.runtimesuite;

import org.junit.Test;

import static org.fest.assertions.Assertions.*;

import com.dhemery.runtimesuite.MethodFilter;
import com.dhemery.runtimesuite.filters.IncludeMethodCategories;

import examples.CategoryA;
import examples.CategoryB;
import examples.ClassWithCategorizedMethods;

public class IncludeMethodCategoriesTest {
	private Class<?> targetClass = ClassWithCategorizedMethods.class;

	@Test
	public void forAnEmptyCategoryList_passesNoMethods() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new IncludeMethodCategories();
		assertThat(filter.passes(targetClass.getMethod("methodWithNoCategories"))).isFalse();
		assertThat(filter.passes(targetClass.getMethod("methodInCategoryA"))).isFalse();
		assertThat(filter.passes(targetClass.getMethod("methodInCategoryB"))).isFalse();
		assertThat(filter.passes(targetClass.getMethod("methodInCategoriesAandB"))).isFalse();
		assertThat(filter.passes(targetClass.getMethod("methodInCategoriesCandD"))).isFalse();
	}

	@Test
	public void forASingleCategory_passesEachMethodInThatCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new IncludeMethodCategories(CategoryA.class);
		assertThat(filter.passes(targetClass.getMethod("methodInCategoryA"))).isTrue();
		assertThat(filter.passes(targetClass.getMethod("methodInCategoriesAandB"))).isTrue();
	}

	@Test
	public void forASingleCategory_rejectsEachMethodNotInThatCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new IncludeMethodCategories(CategoryA.class);
		assertThat(filter.passes(targetClass.getMethod("methodWithNoCategories"))).isFalse();
		assertThat(filter.passes(targetClass.getMethod("methodInCategoryB"))).isFalse();
		assertThat(filter.passes(targetClass.getMethod("methodInCategoriesCandD"))).isFalse();
	}
	
	@Test
	public void forMultipleCategories_passesEachMethodInAnySpecifiedCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new IncludeMethodCategories(CategoryA.class, CategoryB.class);
		assertThat(filter.passes(targetClass.getMethod("methodInCategoryA"))).isTrue();
		assertThat(filter.passes(targetClass.getMethod("methodInCategoryB"))).isTrue();
		assertThat(filter.passes(targetClass.getMethod("methodInCategoriesAandB"))).isTrue();
	}
	
	@Test
	public void forMultipleCategories_rejectsEachMethodThatLacksEverySpecifiedCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new IncludeMethodCategories(CategoryA.class, CategoryB.class);
		assertThat(filter.passes(targetClass.getMethod("methodWithNoCategories"))).isFalse();
		assertThat(filter.passes(targetClass.getMethod("methodInCategoriesCandD"))).isFalse();
	}
}
