package com.dhemery.runtimesuite;

import org.junit.Test;

import static org.fest.assertions.Assertions.*;

import com.dhemery.runtimesuite.MethodFilter;
import com.dhemery.runtimesuite.examples.CategoryA;
import com.dhemery.runtimesuite.examples.CategoryB;
import com.dhemery.runtimesuite.examples.ClassWithCategorizedMethods;
import com.dhemery.runtimesuite.filters.MethodsNotInCategories;

public class MethodsNotInCategoriesFilterTest {
	private Class<?> targetClass = ClassWithCategorizedMethods.class;

	@Test public void forASingleCategory_rejectsEachMethodInThatCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new MethodsNotInCategories(CategoryA.class);
		assertThat(filter.passes(targetClass.getMethod("methodInCategoryA"))).isFalse();
		assertThat(filter.passes(targetClass.getMethod("methodInCategoriesAandB"))).isFalse();
	}

	@Test public void forASingleCategory_passesEachMethodNotInThatCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new MethodsNotInCategories(CategoryA.class);
		assertThat(filter.passes(targetClass.getMethod("methodWithNoCategories"))).isTrue();
		assertThat(filter.passes(targetClass.getMethod("methodInCategoryB"))).isTrue();
		assertThat(filter.passes(targetClass.getMethod("methodInCategoriesCandD"))).isTrue();
	}
	
	@Test public void forMultipleCategories_rejectsEachMethodInAnySpecifiedCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new MethodsNotInCategories(CategoryA.class, CategoryB.class);
		assertThat(filter.passes(targetClass.getMethod("methodInCategoryA"))).isFalse();
		assertThat(filter.passes(targetClass.getMethod("methodInCategoryB"))).isFalse();
		assertThat(filter.passes(targetClass.getMethod("methodInCategoriesAandB"))).isFalse();
	}
	
	@Test public void forMultipleCategories_passesEachMethodThatLacksEverySpecifiedCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new MethodsNotInCategories(CategoryA.class, CategoryB.class);
		assertThat(filter.passes(targetClass.getMethod("methodWithNoCategories"))).isTrue();
		assertThat(filter.passes(targetClass.getMethod("methodInCategoriesCandD"))).isTrue();
	}
}
