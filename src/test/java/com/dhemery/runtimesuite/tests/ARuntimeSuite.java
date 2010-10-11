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

	public static class SuiteWithTwoClassFinders {
		@Finder public ClassFinder classFinder1 = new TestClassFinder(TestClass1.class);
		@Finder public ClassFinder classFinder2 = new TestClassFinder(TestClass2.class);
	}

	public static class SuiteWithTwoClassFilters {
		@Finder public ClassFinder classFinder1 = new TestClassFinder(TestClass1.class, TestClass2.class, TestClass3.class);
		@Filter public ClassFilter classFilter1 = new TestClassRemover(TestClass1.class);
		@Filter public ClassFilter classFilter2 = new TestClassRemover(TestClass3.class);
	} 

	@Test public void gathersTestClassesFromAllClassFinderFieldsAnnotatedWithFinder() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithTwoClassFinders.class, builder);
		List<Class<?>> testClasses = testClassesFrom(suite.getRunners());
		assertThat(testClasses).containsOnly(TestClass1.class, TestClass2.class);
		assertThat(testClasses).hasSize(2);
	}

	@Test public void appliesAllClassFilterFieldsAnnotatedWithFilter() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithTwoClassFilters.class, builder);
		List<Class<?>> testClasses = testClassesFrom(suite.getRunners());
		assertThat(testClasses).containsOnly(TestClass2.class);
		assertThat(testClasses).hasSize(1);
	}

	@Test public void createsARunnerForEachTestClass() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithTwoClassFinders.class, builder);
		List<Runner> runners = suite.getRunners();
		assertThat(runners).hasSize(2);
		List<Class<?>> runnerTestClasses = testClassesFrom(runners);
		assertThat(runnerTestClasses).containsOnly(TestClass1.class, TestClass2.class);
	}

	@Test public void createsRunnersOnlyForTestClassesThatSurviveFilters() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithTwoClassFilters.class, builder);
		List<Runner> runners = suite.getRunners();
		assertThat(runners).hasSize(1);
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
