package com.dhemery.runtimesuite.filters;

import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.fest.assertions.Assertions.*;

import com.dhemery.runtimesuite.MethodFilter;

public class AMethodsNotInCategoriesFilter {
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

	@Test public void forASingleCategory_rejectsEachMethodInThatCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new MethodsNotInCategories(CategoryA.class);
		assertThat(filter.passes(method("methodInCategoryA"))).isFalse();
		assertThat(filter.passes(method("methodInCategoriesAandB"))).isFalse();
	}

	@Test public void forASingleCategory_passesEachMethodNotInThatCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new MethodsNotInCategories(CategoryA.class);
		assertThat(filter.passes(method("methodWithNoCategories"))).isTrue();
		assertThat(filter.passes(method("methodInCategoryB"))).isTrue();
		assertThat(filter.passes(method("methodInCategoriesCandD"))).isTrue();
	}
	
	@Test public void forMultipleCategories_rejectsEachMethodInAnySpecifiedCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new MethodsNotInCategories(CategoryA.class, CategoryB.class);
		assertThat(filter.passes(method("methodInCategoryA"))).isFalse();
		assertThat(filter.passes(method("methodInCategoryB"))).isFalse();
		assertThat(filter.passes(method("methodInCategoriesAandB"))).isFalse();
	}
	
	@Test public void forMultipleCategories_passesEachMethodThatLacksEverySpecifiedCategory() throws SecurityException, NoSuchMethodException {
		MethodFilter filter = new MethodsNotInCategories(CategoryA.class, CategoryB.class);
		assertThat(filter.passes(method("methodWithNoCategories"))).isTrue();
		assertThat(filter.passes(method("methodInCategoriesCandD"))).isTrue();
	}

	private Method method(String name) throws SecurityException, NoSuchMethodException {
		return MyTestClass.class.getMethod(name);
	}	
}
