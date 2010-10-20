package com.dhemery.runtimesuite;

import org.junit.Test;

import static org.fest.assertions.Assertions.*;

import com.dhemery.runtimesuite.MethodFilter;
import com.dhemery.runtimesuite.filters.MethodsInCategories;

import examples.CategoryA;
import examples.CategoryB;
import examples.ClassWithCategorizedMethods;

public class MethodsInCategoriesFilterTest {
	private Class<?> targetClass = ClassWithCategorizedMethods.class;

	@Test public void forASingleCategory_passesEachMethodInThatCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new MethodsInCategories(CategoryA.class);
		assertThat(filter.passes(targetClass.getMethod("methodInCategoryA"))).isTrue();
		assertThat(filter.passes(targetClass.getMethod("methodInCategoriesAandB"))).isTrue();
	}

	@Test public void forASingleCategory_rejectsEachMethodNotInThatCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new MethodsInCategories(CategoryA.class);
		assertThat(filter.passes(targetClass.getMethod("methodWithNoCategories"))).isFalse();
		assertThat(filter.passes(targetClass.getMethod("methodInCategoryB"))).isFalse();
		assertThat(filter.passes(targetClass.getMethod("methodInCategoriesCandD"))).isFalse();
	}
	
	@Test public void forMultipleCategories_passesEachMethodInAnySpecifiedCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new MethodsInCategories(CategoryA.class, CategoryB.class);
		assertThat(filter.passes(targetClass.getMethod("methodInCategoryA"))).isTrue();
		assertThat(filter.passes(targetClass.getMethod("methodInCategoryB"))).isTrue();
		assertThat(filter.passes(targetClass.getMethod("methodInCategoriesAandB"))).isTrue();
	}
	
	@Test public void forMultipleCategories_rejectsEachMethodThatLacksEverySpecifiedCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new MethodsInCategories(CategoryA.class, CategoryB.class);
		assertThat(filter.passes(targetClass.getMethod("methodWithNoCategories"))).isFalse();
		assertThat(filter.passes(targetClass.getMethod("methodInCategoriesCandD"))).isFalse();
	}
}
