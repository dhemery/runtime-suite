package examples;

import org.junit.Test;

import com.dhemery.runtimesuite.filters.Category;

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