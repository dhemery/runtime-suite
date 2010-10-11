package com.dhemery.runtimesuite.tests;

import static org.fest.assertions.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.Filter;
import com.dhemery.runtimesuite.Finder;
import com.dhemery.runtimesuite.RuntimeSuite;

import examples.NotAClassFilter;
import examples.NotAClassFinder;
import examples.TestClass3;
import examples.TestClassFinder;
import examples.TestClass1;
import examples.TestClass2;
import examples.TestClassRemover;

public class ARuntimeSuite {
	private RunnerBuilder builder;
	
	@Before public void setUp() {
		builder = new AllDefaultPossibilitiesBuilder(true);
	}

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

	@Test public void gathersTestClassesFromAllClassFinderFieldsAnnotatedWithFinder() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithTwoFinders.class, builder);
		List<Class<?>> testClasses = testClassesFrom(suite.getRunners());
		assertThat(testClasses).containsOnly(TestClass1.class, TestClass2.class);
		assertThat(testClasses).hasSize(2);
	}

	@Test public void ignoresClassFinderFieldsThatLackFinderAnnotation() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithFindersNotToRun.class, builder);
		assertThat(testClassesFrom(suite.getRunners())).excludes(TestClass1.class);
	}

	@Test public void ignoresNonClassFinderFieldsAnnotatedWithFinder() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithFindersNotToRun.class, builder);
		assertThat(testClassesFrom(suite.getRunners())).excludes(TestClass2.class);
	}

	@Test public void runsCorrectlyDeclaredFindersEvenIfTheSuiteHasIncompletelyDeclaredFinders() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithFindersNotToRun.class, builder);
		assertThat(testClassesFrom(suite.getRunners())).containsOnly(TestClass3.class);
	}

	@Test public void runsClassFinderFieldsDeclaredAsDerivedTypes() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithFieldsDeclaredAsSubtypes.class, builder);
		assertThat(testClassesFrom(suite.getRunners())).contains(TestClass1.class, TestClass3.class);
	}

	@Test public void appliesAllClassFilterFieldsAnnotatedWithFilter() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithTwoFilters.class, builder);
		List<Class<?>> testClasses = testClassesFrom(suite.getRunners());
		assertThat(testClasses).containsOnly(TestClass2.class);
	}

	@Test public void ignoresClassFilterFieldsThatLackFilterAnnotation() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithFiltersNotToRun.class, builder);
		assertThat(testClassesFrom(suite.getRunners())).contains(TestClass1.class);
	}

	@Test public void ignoresNonClassFilterFieldsAnnotatedWithFilter() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithFiltersNotToRun.class, builder);
		assertThat(testClassesFrom(suite.getRunners())).contains(TestClass2.class);
	}

	@Test public void runsCorrectlyDeclaredFiltersEvenIfTheSuiteHasIncompletelyDeclaredFilters() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithFiltersNotToRun.class, builder);
		assertThat(testClassesFrom(suite.getRunners())).excludes(TestClass3.class);
	}

	@Test public void runsClassFilterFieldsDeclaredAsDerivedTypes() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithFieldsDeclaredAsSubtypes.class, builder);
		assertThat(testClassesFrom(suite.getRunners())).excludes(TestClass2.class);
	}

	@Test public void createsARunnerForEachTestClass() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithTwoFinders.class, builder);
		List<Runner> runners = suite.getRunners();
		assertThat(testClassesFrom(runners)).contains(TestClass1.class, TestClass2.class);
	}

	@Test public void createsRunnersOnlyForTestClassesThatSurviveFilters() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithTwoFilters.class, builder);
		List<Runner> runners = suite.getRunners();
		assertThat(testClassesFrom(runners)).containsOnly(TestClass2.class);
	}
	
	private List<Class<?>> testClassesFrom(List<Runner> runners) {
		List<Class<?>> runnerTestClasses = new ArrayList<Class<?>>();
		for(Runner runner : runners) {
			runnerTestClasses.add(runner.getDescription().getTestClass());
		}
		return runnerTestClasses;
	}
}
