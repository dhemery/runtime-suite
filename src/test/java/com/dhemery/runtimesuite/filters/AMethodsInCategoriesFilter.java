package com.dhemery.runtimesuite.filters;

import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.fest.assertions.Assertions.*;

import com.dhemery.runtimesuite.MethodFilter;

public class AMethodsInCategoriesFilter {
	public interface CategoryA {}
	public interface CategoryB {}
	public interface CategoryC {}
	public interface CategoryD {}

	public class MyTestClass {
		@Test public void methodWithNoCategories() {}

		@Category(CategoryA.class)
		@Test public void methodInCategoryA() {}

		@Category(CategoryB.class)
		@Test public void methodInCategoryB() {}

		@Category({CategoryA.class, CategoryB.class})
		@Test public void methodInCategoriesAandB() {}

		@Category({CategoryC.class, CategoryD.class})
		@Test public void methodInCategoriesCandD() {}		
	}

	@Test public void forASingleCategory_passesEachMethodInThatCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new MethodsInCategories(CategoryA.class);
		assertThat(filter.passes(method("methodInCategoryA"))).isTrue();
		assertThat(filter.passes(method("methodInCategoriesAandB"))).isTrue();
	}

	@Test public void forASingleCategory_rejectsEachMethodNotInThatCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new MethodsInCategories(CategoryA.class);
		assertThat(filter.passes(method("methodWithNoCategories"))).isFalse();
		assertThat(filter.passes(method("methodInCategoryB"))).isFalse();
		assertThat(filter.passes(method("methodInCategoriesCandD"))).isFalse();
	}
	
	@Test public void forMultipleCategories_passesEachMethodInAnySpecifiedCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new MethodsInCategories(CategoryA.class, CategoryB.class);
		assertThat(filter.passes(method("methodInCategoryA"))).isTrue();
		assertThat(filter.passes(method("methodInCategoryB"))).isTrue();
		assertThat(filter.passes(method("methodInCategoriesAandB"))).isTrue();
	}
	
	@Test public void forMultipleCategories_rejectsEachMethodThatLacksEverySpecifiedCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new MethodsInCategories(CategoryA.class, CategoryB.class);
		assertThat(filter.passes(method("methodWithNoCategories"))).isFalse();
		assertThat(filter.passes(method("methodInCategoriesCandD"))).isFalse();
	}

	private Method method(String name) throws SecurityException, NoSuchMethodException {
		return MyTestClass.class.getMethod(name);
	}	
}
