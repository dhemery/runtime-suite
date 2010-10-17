package com.dhemery.runtimesuite;

import static org.fest.assertions.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;


import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.Filter;
import com.dhemery.runtimesuite.Finder;
import com.dhemery.runtimesuite.RuntimeSuite;
import com.dhemery.runtimesuite.samples.NotAClassFilter;
import com.dhemery.runtimesuite.samples.NotAClassFinder;
import com.dhemery.runtimesuite.samples.NotATestClass;
import com.dhemery.runtimesuite.samples.TestClass1;
import com.dhemery.runtimesuite.samples.TestClass2;
import com.dhemery.runtimesuite.samples.TestClass3;
import com.dhemery.runtimesuite.samples.TestClassFinder;
import com.dhemery.runtimesuite.samples.TestClassRemover;



public class ARuntimeSuite {
	public static class SuiteWithTwoFinders {
		@Finder public ClassFinder classFinder1 = new TestClassFinder(TestClass1.class);
		@Finder public ClassFinder classFinder2 = new TestClassFinder(TestClass2.class);
	}

	public static class SuiteWithTwoFilters {
		@Finder public ClassFinder classFinder1 = new TestClassFinder(TestClass1.class, TestClass2.class, TestClass3.class);
		@Filter public ClassFilter classFilter1 = new TestClassRemover(TestClass1.class);
		@Filter public ClassFilter classFilter2 = new TestClassRemover(TestClass3.class);
	} 

	public static class SuiteWithFindersNotToRun {
		public ClassFinder classFinderTypeButNoFinderAnnotation = new TestClassFinder(TestClass1.class);
		@Finder public NotAClassFinder finderAnnotationButNotClassFinderType = new NotAClassFinder(TestClass2.class);
		@Finder public ClassFinder classFinderTypeWithFinderAnnotation = new TestClassFinder(TestClass3.class);
	}

	public static class SuiteWithFiltersNotToRun {
		@Finder public ClassFinder finder = new TestClassFinder(TestClass1.class, TestClass2.class, TestClass3.class);
		public ClassFilter classFilterTypeButNoFilterAnnotation = new TestClassRemover(TestClass1.class);
		@Filter public NotAClassFilter filterAnnotationButNotClassFilterType = new NotAClassFilter(TestClass2.class);
		@Filter public ClassFilter classFilterTypeWithFilterAnnotation = new TestClassRemover(TestClass3.class);
	}

	public static class SuiteWithFieldsDeclaredAsSubtypes {
		@Finder public TestClassFinder finder = new TestClassFinder(TestClass1.class, TestClass2.class, TestClass3.class);
		@Filter public TestClassRemover filter = new TestClassRemover(TestClass2.class);
	}

	public static class SuiteThatFindsATestClassSeveralTimes {
		@Finder public ClassFinder finder1 = new TestClassFinder(TestClass1.class);
		@Finder public ClassFinder finder2 = new TestClassFinder(TestClass1.class);
		@Finder public ClassFinder finder3 = new TestClassFinder(TestClass1.class);
	}

	public static class SuiteThatFindsNonTestClasses {
		@Finder public ClassFinder nonTestClassFinder = new TestClassFinder(NotATestClass.class);
	}

	@Test public void gathersTestClassesFromAllClassFinderFieldsAnnotatedWithFinder() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithTwoFinders.class);
		List<Class<?>> testClasses = testClassesFrom(suite.getRunners());
		assertThat(testClasses).containsOnly(TestClass1.class, TestClass2.class);
		assertThat(testClasses).hasSize(2);
	}

	@Test public void ignoresClassFinderFieldsThatLackFinderAnnotation() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithFindersNotToRun.class);
		assertThat(testClassesFrom(suite.getRunners())).excludes(TestClass1.class);
	}

	@Test public void ignoresNonClassFinderFieldsAnnotatedWithFinder() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithFindersNotToRun.class);
		assertThat(testClassesFrom(suite.getRunners())).excludes(TestClass2.class);
	}

	@Test public void runsCorrectlyDeclaredFindersEvenIfTheSuiteHasIncompletelyDeclaredFinders() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithFindersNotToRun.class);
		assertThat(testClassesFrom(suite.getRunners())).containsOnly(TestClass3.class);
	}

	@Test public void runsClassFinderFieldsDeclaredAsDerivedTypes() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithFieldsDeclaredAsSubtypes.class);
		assertThat(testClassesFrom(suite.getRunners())).contains(TestClass1.class, TestClass3.class);
	}

	@Test public void appliesAllClassFilterFieldsAnnotatedWithFilter() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithTwoFilters.class);
		List<Class<?>> testClasses = testClassesFrom(suite.getRunners());
		assertThat(testClasses).containsOnly(TestClass2.class);
	}

	@Test public void ignoresClassFilterFieldsThatLackFilterAnnotation() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithFiltersNotToRun.class);
		assertThat(testClassesFrom(suite.getRunners())).contains(TestClass1.class);
	}

	@Test public void ignoresNonClassFilterFieldsAnnotatedWithFilter() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithFiltersNotToRun.class);
		assertThat(testClassesFrom(suite.getRunners())).contains(TestClass2.class);
	}

	@Test public void runsCorrectlyDeclaredFiltersEvenIfTheSuiteHasIncompletelyDeclaredFilters() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithFiltersNotToRun.class);
		assertThat(testClassesFrom(suite.getRunners())).excludes(TestClass3.class);
	}

	@Test public void runsClassFilterFieldsDeclaredAsDerivedTypes() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithFieldsDeclaredAsSubtypes.class);
		assertThat(testClassesFrom(suite.getRunners())).excludes(TestClass2.class);
	}

	@Test public void createsARunnerForEachTestClass() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithTwoFinders.class);
		List<Runner> runners = suite.getRunners();
		assertThat(testClassesFrom(runners)).contains(TestClass1.class, TestClass2.class);
	}

	@Test public void createsOnlyOneRunnerPerTestClassEvenIfFindersFindItMultipleTimes() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteThatFindsATestClassSeveralTimes.class);
		List<Runner> runners = suite.getRunners();
		List<Class<?>> testClasses = testClassesFrom(runners);
		assertThat(testClasses).contains(TestClass1.class);
		assertThat(testClasses).hasSize(1);
	}

	@Test public void createsRunnersOnlyForTestClassesThatSurviveFilters() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithTwoFilters.class);
		List<Runner> runners = suite.getRunners();
		assertThat(testClassesFrom(runners)).containsOnly(TestClass2.class);
	}
	
	@Ignore("Make RuntimeSuite filter out non-test classes")
	@Test public void createsRunnersOnlyForTestClasses() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteThatFindsNonTestClasses.class);
		List<Runner> runners = suite.getRunners();
		assertThat(testClassesFrom(runners)).excludes(NotATestClass.class);
	}

	private List<Class<?>> testClassesFrom(List<Runner> runners) {
		List<Class<?>> runnerTestClasses = new ArrayList<Class<?>>();
		for(Runner runner : runners) {
			runnerTestClasses.add(runner.getDescription().getTestClass());
		}
		return runnerTestClasses;
	}
}
