package com.dhemery.runtimesuite;

import static org.fest.assertions.Assertions.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;


import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.Filter;
import com.dhemery.runtimesuite.Finder;
import com.dhemery.runtimesuite.RuntimeSuite;
import com.dhemery.runtimesuite.finders.ListedClasses;

public class RuntimeSuiteMethodFilterTest {
	public static List<String> executedTests;

	@Before
	public void resetExecutedTestList() {
		executedTests = new ArrayList<String>();
	}

	public static class Runnable {
		@Test public void a_test1() { executedTests.add("a_test1"); }
		@Test public void a_test2() { executedTests.add("a_test2"); }
		@Test public void b_test1() { executedTests.add("b_test1"); }
		@Test public void b_test2() { executedTests.add("b_test2"); }
	}
	
	public static class NotRunnable {
		public void noTestAnnotation() {  executedTests.add("noTestAnnotation"); }
		@Test public int nonVoidReturnType() {  executedTests.add("nonVoidReturnType"); return 0; }
		@Test public void takesParameters(int i) { executedTests.add("takesParameters"); }
		@Test void notPublic() { executedTests.add("notPublic"); }
	}

	@RunWith(RuntimeSuite.class)
	public static class SuiteWithNoMethodFilters {
		@Finder public ClassFinder finder = new ListedClasses(Runnable.class);
	}

	@Test
	public void runsAllMethodsIfNoFilters() {
		new JUnitCore().run(SuiteWithNoMethodFilters.class);
		assertThat(executedTests).contains("a_test1", "a_test2", "b_test1", "b_test2");
	}

	public static class MethodsThatStartWith implements MethodFilter {
		private final String prefix;
		public MethodsThatStartWith(String prefix) {
			this.prefix = prefix;
		}
		public boolean passes(Method method) {
			return method.getName().startsWith(prefix);
		}
	}
	
	public static class MethodsThatEndWith implements MethodFilter {
		private final String suffix;
		public MethodsThatEndWith(String suffix) {
			this.suffix = suffix;
		}
		public boolean passes(Method method) {
			return method.getName().endsWith(suffix);
		}
	}
	
	@RunWith(RuntimeSuite.class)
	public static class SuiteWithMethodFilters {
		@Finder public ClassFinder finder = new ListedClasses(Runnable.class);
		@Filter public MethodFilter startWithA = new MethodsThatStartWith("a_");
		@Filter public MethodFilter endWith1 = new MethodsThatEndWith("1");
	}

	@Test
	public void runsOnlyTestMethodsThatSurviveMethodFilters() {
		new JUnitCore().run(SuiteWithMethodFilters.class);
		assertThat(executedTests).containsOnly("a_test1");
	}

	@RunWith(RuntimeSuite.class)
	public static class SuiteThatFindsNonTestClasses {
		@Finder public ClassFinder nonTestClassFinder = new ListedClasses(NotRunnable.class);
	}

	@Test
	public void ignoresNonTestMethods() {
		new JUnitCore().run(SuiteWithMethodFilters.class);
		assertThat(executedTests).excludes("noTestAnnotation");
		assertThat(executedTests).excludes("nonVoidReturnType");
		assertThat(executedTests).excludes("takesParameters");
		assertThat(executedTests).excludes("notPublic");
	}
}
