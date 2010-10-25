package com.dhemery.runtimesuite;

import static org.fest.assertions.Assertions.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;


import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.Filter;
import com.dhemery.runtimesuite.Finder;
import com.dhemery.runtimesuite.RuntimeSuite;
import com.dhemery.runtimesuite.finders.ListedClasses;

import examples.ListedClassNonFinder;
import examples.ListedClassRejecterFilter;
import examples.ListedClassRejecterNonFilter;
import examples.MethodNamePrefixFilter;
import examples.NotRunnable;
import examples.Runnable1;
import examples.Runnable2;
import examples.Runnable3;
import examples.RunnableWithFilterableMethodNames;


public class RuntimeSuiteTest {
	public static class SuiteWithTwoFinders {
		@Finder public ClassFinder classFinder1 = new ListedClasses(Runnable1.class);
		@Finder public ClassFinder classFinder2 = new ListedClasses(Runnable2.class);
	}

	public static class SuiteWithTwoFilters {
		@Finder public ClassFinder classFinder1 = new ListedClasses(Runnable1.class, Runnable2.class, Runnable3.class);
		@Filter public ClassFilter classFilter1 = new ListedClassRejecterFilter(Runnable1.class);
		@Filter public ClassFilter classFilter2 = new ListedClassRejecterFilter(Runnable3.class);
	} 

	public static class SuiteWithFindersNotToRun {
		public ClassFinder classFinderTypeButNoFinderAnnotation = new ListedClasses(Runnable1.class);
		@Finder public ListedClassNonFinder finderAnnotationButNotClassFinderType = new ListedClassNonFinder(Runnable2.class);
		@Finder public ClassFinder classFinderTypeWithFinderAnnotation = new ListedClasses(Runnable3.class);
	}

	public static class SuiteWithFiltersNotToRun {
		@Finder public ClassFinder finder = new ListedClasses(Runnable1.class, Runnable2.class, Runnable3.class);
		public ClassFilter classFilterTypeButNoFilterAnnotation = new ListedClassRejecterFilter(Runnable1.class);
		@Filter public ListedClassRejecterNonFilter filterAnnotationButNotClassFilterType = new ListedClassRejecterNonFilter(Runnable2.class);
		@Filter public ClassFilter classFilterTypeWithFilterAnnotation = new ListedClassRejecterFilter(Runnable3.class);
	}

	public static class SuiteWithFieldsDeclaredAsSubtypes {
		@Finder public ListedClasses finder = new ListedClasses(Runnable1.class, Runnable2.class, Runnable3.class);
		@Filter public ListedClassRejecterFilter filter = new ListedClassRejecterFilter(Runnable2.class);
	}

	public static class SuiteThatFindsATestClassSeveralTimes {
		@Finder public ClassFinder finder1 = new ListedClasses(Runnable1.class);
		@Finder public ClassFinder finder2 = new ListedClasses(Runnable1.class);
		@Finder public ClassFinder finder3 = new ListedClasses(Runnable1.class);
	}

	public static class SuiteThatFindsNonTestClasses {
		@Finder public ClassFinder nonTestClassFinder = new ListedClasses(NotRunnable.class);
	}

	public static class SuiteWithMethodFilters {
		@Finder public ClassFinder finder = new ListedClasses(RunnableWithFilterableMethodNames.class);
		@Filter public MethodFilter filter = new MethodNamePrefixFilter("a_");
	}

	@Test public void gathersTestClassesFromAllClassFinderFieldsAnnotatedWithFinder() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithTwoFinders.class);
		Collection<Class<?>> testClasses = testClassesFrom(suite.getRunners());
		assertThat(testClasses).containsOnly(Runnable1.class, Runnable2.class);
	}

	@Test public void ignoresClassFinderFieldsThatLackFinderAnnotation() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithFindersNotToRun.class);
		assertThat(testClassesFrom(suite.getRunners())).excludes(Runnable1.class);
	}

	@Test public void ignoresNonClassFinderFieldsAnnotatedWithFinder() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithFindersNotToRun.class);
		assertThat(testClassesFrom(suite.getRunners())).excludes(Runnable2.class);
	}

	@Test public void runsCorrectlyDeclaredFindersEvenIfTheSuiteHasIncompletelyDeclaredFinders() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithFindersNotToRun.class);
		assertThat(testClassesFrom(suite.getRunners())).containsOnly(Runnable3.class);
	}

	@Test public void runsClassFinderFieldsDeclaredAsDerivedTypes() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithFieldsDeclaredAsSubtypes.class);
		assertThat(testClassesFrom(suite.getRunners())).contains(Runnable1.class, Runnable3.class);
	}

	@Test public void appliesAllClassFilterFieldsAnnotatedWithFilter() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithTwoFilters.class);
		Collection<Class<?>> testClasses = testClassesFrom(suite.getRunners());
		assertThat(testClasses).containsOnly(Runnable2.class);
	}

	@Test public void ignoresClassFilterFieldsThatLackFilterAnnotation() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithFiltersNotToRun.class);
		assertThat(testClassesFrom(suite.getRunners())).contains(Runnable1.class);
	}

	@Test public void ignoresNonClassFilterFieldsAnnotatedWithFilter() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithFiltersNotToRun.class);
		assertThat(testClassesFrom(suite.getRunners())).contains(Runnable2.class);
	}

	@Test public void runsCorrectlyDeclaredFiltersEvenIfTheSuiteHasIncompletelyDeclaredFilters() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithFiltersNotToRun.class);
		assertThat(testClassesFrom(suite.getRunners())).excludes(Runnable3.class);
	}

	@Test public void runsClassFilterFieldsDeclaredAsDerivedTypes() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithFieldsDeclaredAsSubtypes.class);
		assertThat(testClassesFrom(suite.getRunners())).excludes(Runnable2.class);
	}

	@Test public void createsARunnerForEachTestClass() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithTwoFinders.class);
		Collection<Runner> runners = suite.getRunners();
		assertThat(testClassesFrom(runners)).contains(Runnable1.class, Runnable2.class);
	}

	@Test public void createsOnlyOneRunnerPerTestClassEvenIfFindersFindItMultipleTimes() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteThatFindsATestClassSeveralTimes.class);
		Collection<Runner> runners = suite.getRunners();
		Collection<Class<?>> testClasses = testClassesFrom(runners);
		assertThat(testClasses).contains(Runnable1.class);
		assertThat(testClasses).hasSize(1);
	}

	@Test public void createsRunnersOnlyForTestClassesThatSurviveClassFilters() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithTwoFilters.class);
		Collection<Runner> runners = suite.getRunners();
		assertThat(testClassesFrom(runners)).containsOnly(Runnable2.class);
	}
	
	public void createsRunnersOnlyForTestClasses() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteThatFindsNonTestClasses.class);
		Collection<Runner> runners = suite.getRunners();
		assertThat(testClassesFrom(runners)).excludes(NotRunnable.class);
	}

	@Test public void createsRunnersOnlyForMethodsThatSurviveMethodFilters() throws InitializationError, SecurityException, NoSuchMethodException {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithMethodFilters.class);
		Collection<Runner> runners = suite.getRunners();
		Collection<Method> testMethods = testMethodsFrom(runners);
		assertThat(testMethods).excludes(method(RunnableWithFilterableMethodNames.class, "b_test1"),
										method(RunnableWithFilterableMethodNames.class, "b_test2"));
	}

	@Test public void createsRunnersForAllMethodsThatSurviveMethodFilters() throws InitializationError, SecurityException, NoSuchMethodException {
		RuntimeSuite suite = new RuntimeSuite(SuiteWithMethodFilters.class);
		Collection<Runner> runners = suite.getRunners();
		Collection<Method> testMethods = testMethodsFrom(runners);
		assertThat(testMethods).containsOnly(method(RunnableWithFilterableMethodNames.class, "a_test1"),
											method(RunnableWithFilterableMethodNames.class, "a_test2"));
	}

	private Collection<Method> testMethodsFrom(Collection<Runner> runners) {
		Collection<Method> methods = new ArrayList<Method>();
		for(Runner runner : runners) {
			methods.addAll(testMethodsFrom(runner.getDescription()));			
		}
		return methods;
	}

	private Collection<Method> testMethodsFrom(Description description) {
		if(description.isTest()) {
			Class<?> c = description.getTestClass();
			String methodName = description.getMethodName();
			return Arrays.asList(new Method[] { method(c, methodName) });
		}
		if(description.isSuite()) {
			return testMethodsFromSuite(description);
		}
		return Collections.emptyList();
	}

	private Method method(Class<?> c, String methodName) {
		try {
			return c.getMethod(methodName);
		} catch (Exception e) {
			return null;
		}
	}

	private Collection<Method> testMethodsFromSuite(Description suite) {
		Collection<Method> methods = new ArrayList<Method>();
		for(Description child : suite.getChildren()) {
			methods.addAll(testMethodsFrom(child));
		}
		return methods;
	}

	private Collection<Class<?>> testClassesFrom(Collection<Runner> runners) {
		Collection<Class<?>> runnerTestClasses = new ArrayList<Class<?>>();
		for(Runner runner : runners) {
			runnerTestClasses.add(runner.getDescription().getTestClass());
		}
		return runnerTestClasses;
	}
}
