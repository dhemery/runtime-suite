package example.tests;

import org.junit.Test;

import com.dhemery.runtimesuite.filters.Category;

import example.categories.CategoryA;
import example.categories.CategoryB;
import example.categories.CategoryC;
import example.categories.CategoryD;

public class ClassWithCategorizedMethods {
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