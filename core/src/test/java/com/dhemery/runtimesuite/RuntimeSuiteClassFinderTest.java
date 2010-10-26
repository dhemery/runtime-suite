package com.dhemery.runtimesuite;

import static org.fest.assertions.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.model.InitializationError;

import com.dhemery.runtimesuite.ClassFinder;
import com.dhemery.runtimesuite.Finder;
import com.dhemery.runtimesuite.RuntimeSuite;
import com.dhemery.runtimesuite.finders.ListedClasses;

public class RuntimeSuiteClassFinderTest {
	public static List<String> executedTests;
	
	@Before public void resetExecutedTestList() {
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
	public static class SuiteWithTwoFinders {
		@Finder public ClassFinder classFinder1 = new ListedClasses(Runnable1.class);
		@Finder public ClassFinder classFinder2 = new ListedClasses(Runnable2.class);
	}

	@Test
	public void runsTestsFromAllFinders() throws InitializationError {
		new JUnitCore().run(SuiteWithTwoFinders.class);
		assertThat(executedTests).contains("Runnable1", "Runnable2"); 
	}

	@RunWith(RuntimeSuite.class)
	public static class SuiteThatFindsATestClassSeveralTimes {
		@Finder public ClassFinder finder1 = new ListedClasses(Runnable1.class);
		@Finder public ClassFinder finder2 = new ListedClasses(Runnable1.class);
		@Finder public ClassFinder finder3 = new ListedClasses(Runnable1.class);
	}

	@Test public void runsEachTestOnceEvenIfFoundMultipleTimes() throws InitializationError {
		new JUnitCore().run(SuiteThatFindsATestClassSeveralTimes.class);
		assertThat(executedTests).doesNotHaveDuplicates();
	}

	public static class NotAClassFinder /* Does not implement ClassFinder */ {
		public Collection<Class<?>> find() {
			return Arrays.asList(new Class<?>[] { Runnable2.class });
		}
	}

	@RunWith(RuntimeSuite.class)
	public static class SuiteWithGoodAndBadFinders {
		public ClassFinder badFinderNoAnnotation = new ListedClasses(Runnable1.class);
		@Finder public NotAClassFinder badFinderWrongType = new NotAClassFinder();
		@Finder public ClassFinder goodFinder = new ListedClasses(Runnable3.class);
	}

	@Test
	public void runsTestsOnlyFromCorrectlyDeclaredFinders() throws InitializationError {
		new JUnitCore().run(SuiteWithGoodAndBadFinders.class);
		assertThat(executedTests).containsOnly("Runnable3"); 
	}

	@RunWith(RuntimeSuite.class)
	public static class SuiteWithFinderDeclaredAsSubtype {
		@Finder public ListedClasses finder = new ListedClasses(Runnable1.class, Runnable2.class, Runnable3.class);
	}

	@Test
	public void runsTestsFromFinderFieldsDeclaredAsDerivedTypes() throws InitializationError {
		new JUnitCore().run(SuiteWithFinderDeclaredAsSubtype.class);
		assertThat(executedTests).contains("Runnable1", "Runnable2", "Runnable3"); 
	}
}
