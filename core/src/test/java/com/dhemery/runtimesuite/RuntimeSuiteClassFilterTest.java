package com.dhemery.runtimesuite;

import static org.fest.assertions.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.model.InitializationError;


import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.Filter;
import com.dhemery.runtimesuite.Finder;
import com.dhemery.runtimesuite.RuntimeSuite;
import com.dhemery.runtimesuite.finders.ListedClasses;

public class RuntimeSuiteClassFilterTest {
	public static List<String> executedTests;

	@Before
	public void resetExecutedTestList() {
		executedTests = new ArrayList<String>();
	}
	
	public static class Runnable1 {
		@Test public void myTest() { executedTests.add("Runnable1"); }
	}

	public static class Runnable2 {
		@Test public void myTest() { executedTests.add("Runnable2"); }
	}

	public static class Runnable3 {
		@Test public void myTest() { executedTests.add("Runnable3"); }
	}

	@RunWith(RuntimeSuite.class)
	public static class SuiteWithTwoFilters {
		@Finder public ClassFinder classFinder1 = new ListedClasses(Runnable1.class, Runnable2.class, Runnable3.class);
		@Filter public ClassFilter classFilter1 = new ExcludeClasses(Runnable1.class);
		@Filter public ClassFilter classFilter2 = new ExcludeClasses(Runnable3.class);
	} 

	@Test
	public void appliesAllClassFilterFieldsAnnotatedWithFilter() throws InitializationError {
		new JUnitCore().run(SuiteWithTwoFilters.class);
		assertThat(executedTests).containsOnly("Runnable2");
	}

	public static class ExcludeClassesNonFilter /* Does not implement ClassFilter */ {
		private List<Class<?>> classesToRemove;
		public ExcludeClassesNonFilter(Class<?>...classesToRemove) {
			this.classesToRemove = Arrays.asList(classesToRemove);
		}
		public boolean passes(Class<?> candidateClass) {
			return !classesToRemove.contains(candidateClass);
		}
	}

	@RunWith(RuntimeSuite.class)
	public static class SuiteWithFiltersNotToRun {
		@Finder public ClassFinder finder = new ListedClasses(Runnable1.class, Runnable2.class, Runnable3.class);
		public ClassFilter badFilterNoAnnotation = new ExcludeClasses(Runnable1.class);
		@Filter public ExcludeClassesNonFilter badFilterWrongType = new ExcludeClassesNonFilter(Runnable2.class);
		@Filter public ClassFilter goodFilter = new ExcludeClasses(Runnable3.class);
	}

	@Test
	public void appliesOnlyCorrectlyDeclaredClassFilters() throws InitializationError {
		new JUnitCore().run(SuiteWithFiltersNotToRun.class);
		assertThat(executedTests).containsOnly("Runnable1", "Runnable2");
	}

	@RunWith(RuntimeSuite.class)
	public static class SuiteWithFiltersDeclaredAsSubtypes {
		@Finder public ClassFinder finder = new ListedClasses(Runnable1.class, Runnable2.class, Runnable3.class);
		@Filter public ExcludeClasses filter = new ExcludeClasses(Runnable2.class);
	}

	@Test
	public void appliesClassFilterFieldsDeclaredAsDerivedTypes() throws InitializationError {
		new JUnitCore().run(SuiteWithFiltersDeclaredAsSubtypes.class);
		assertThat(executedTests).excludes("Runnable2");
	}
}
