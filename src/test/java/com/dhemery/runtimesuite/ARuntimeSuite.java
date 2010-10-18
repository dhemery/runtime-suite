package com.dhemery.runtimesuite;

import static org.fest.assertions.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;


import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.Filter;
import com.dhemery.runtimesuite.Finder;
import com.dhemery.runtimesuite.RuntimeSuite;



public class ARuntimeSuite {
	public static class SuiteWithTwoFinders {
		@Finder public ClassFinder classFinder1 = new ListedClassFinder(TestClass1.class);
		@Finder public ClassFinder classFinder2 = new ListedClassFinder(TestClass2.class);
	}

	public static class SuiteWithTwoFilters {
		@Finder public ClassFinder classFinder1 = new ListedClassFinder(TestClass1.class, TestClass2.class, TestClass3.class);
		@Filter public ClassFilter classFilter1 = new ListedClassRejecterFilter(TestClass1.class);
		@Filter public ClassFilter classFilter2 = new ListedClassRejecterFilter(TestClass3.class);
	} 

	public static class SuiteWithFindersNotToRun {
		public ClassFinder classFinderTypeButNoFinderAnnotation = new ListedClassFinder(TestClass1.class);
		@Finder public ListedClassNonFinder finderAnnotationButNotClassFinderType = new ListedClassNonFinder(TestClass2.class);
		@Finder public ClassFinder classFinderTypeWithFinderAnnotation = new ListedClassFinder(TestClass3.class);
	}

	public static class SuiteWithFiltersNotToRun {
		@Finder public ClassFinder finder = new ListedClassFinder(TestClass1.class, TestClass2.class, TestClass3.class);
		public ClassFilter classFilterTypeButNoFilterAnnotation = new ListedClassRejecterFilter(TestClass1.class);
		@Filter public ListedClassRejecterNonFilter filterAnnotationButNotClassFilterType = new ListedClassRejecterNonFilter(TestClass2.class);
		@Filter public ClassFilter classFilterTypeWithFilterAnnotation = new ListedClassRejecterFilter(TestClass3.class);
	}

	public static class SuiteWithFieldsDeclaredAsSubtypes {
		@Finder public ListedClassFinder finder = new ListedClassFinder(TestClass1.class, TestClass2.class, TestClass3.class);
		@Filter public ListedClassRejecterFilter filter = new ListedClassRejecterFilter(TestClass2.class);
	}

	public static class SuiteThatFindsATestClassSeveralTimes {
		@Finder public ClassFinder finder1 = new ListedClassFinder(TestClass1.class);
		@Finder public ClassFinder finder2 = new ListedClassFinder(TestClass1.class);
		@Finder public ClassFinder finder3 = new ListedClassFinder(TestClass1.class);
	}

	public static class SuiteThatFindsNonTestClasses {
		@Finder public ClassFinder nonTestClassFinder = new ListedClassFinder(TestlessClass.class);
	}

	public static class ListedClassNonFinder /* Does not implement ClassFinder */ {
		private Collection<Class<?>> classes;

		public ListedClassNonFinder(Class<?>...classes) {
			this.classes = Arrays.asList(classes);
		}

		public Collection<Class<?>> find() {
			return classes;
		}
	}

	public static class ListedClassFinder extends ListedClassNonFinder implements ClassFinder {
		public ListedClassFinder(Class<?>...classes) {
			super(classes);
		}
	}

	public static class ListedClassRejecterNonFilter /* Does not implement ClassFilter */ {
		private List<Class<?>> classesToRemove;

		public ListedClassRejecterNonFilter(Class<?>...classesToRemove) {
			this.classesToRemove = Arrays.asList(classesToRemove);
		}

		public boolean passes(Class<?> candidateClass) {
			return !classesToRemove.contains(candidateClass);
		}
	}

	public static class ListedClassRejecterFilter extends ListedClassRejecterNonFilter implements ClassFilter {
		public ListedClassRejecterFilter(Class<?>...classesToRemove) {
			super(classesToRemove);
		}
	}

	public class TestClass1 {
		@Test public void myTest1() {}
	}

	public class TestClass2 {
		@Test public void myTest2() {}
	}

	public class TestClass3 {
		@Test public void myTest3() {}
	}

	public class TestlessClass {
		public void noTestAnnotation() {}
		@Test public int nonVoidReturnType() { return 0; }
		@Test public void takesParameters(int i) {}
		@Test void notPublic() {}
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
	
	@Test public void createsRunnersOnlyForTestClasses() throws InitializationError {
		RuntimeSuite suite = new RuntimeSuite(SuiteThatFindsNonTestClasses.class);
		List<Runner> runners = suite.getRunners();
		assertThat(testClassesFrom(runners)).excludes(TestlessClass.class);
	}

	private List<Class<?>> testClassesFrom(List<Runner> runners) {
		List<Class<?>> runnerTestClasses = new ArrayList<Class<?>>();
		for(Runner runner : runners) {
			runnerTestClasses.add(runner.getDescription().getTestClass());
		}
		return runnerTestClasses;
	}
}
