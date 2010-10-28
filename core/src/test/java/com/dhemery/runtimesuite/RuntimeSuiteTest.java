package com.dhemery.runtimesuite;

import static org.fest.assertions.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.RunListener;

import examples.SuiteThatFiltersOutAllClassesButOne;
import examples.SuiteThatFindsATestClassSeveralTimes;
import examples.SuiteWithAllBadTests;
import examples.SuiteWithClassFilterDeclaredAsSubtype;
import examples.SuiteWithClassFinderDeclaredAsSubtype;
import examples.SuiteWithGoodAndBadClassFilters;
import examples.SuiteWithGoodAndBadClassFinders;
import examples.SuiteWithMethodFilters;
import examples.SuiteWithNoMethodFilters;
import examples.SuiteWithTwoFinders;

public class RuntimeSuiteTest {
	private JUnitCore runner;
	private SuiteRunListener executed;

	public class SuiteRunListener extends RunListener {
		public final List<String> tests = new ArrayList<String>();

		@Override
		public void testStarted(Description description) {
			tests.add(description.getMethodName());
		}
	}

	@Before
	public void setUp() {
		executed = new SuiteRunListener();
		runner = new JUnitCore();
		runner.addListener(executed);
	}
	
	@Test
	public void runsTestsFromAllFinders() {
		runner.run(SuiteWithTwoFinders.class);
		assertThat(executed.tests).contains("runnable2", "runnable2"); 
	}

	@Test
	public void runsEachTestOnceEvenIfFoundMultipleTimes() {
		runner.run(SuiteThatFindsATestClassSeveralTimes.class);
		assertThat(executed.tests).doesNotHaveDuplicates();
	}

	@Test
	public void runsTestsOnlyFromCorrectlyDeclaredFinders() {
		runner.run(SuiteWithGoodAndBadClassFinders.class);
		assertThat(executed.tests).containsOnly("runnable3"); 
	}

	@Test
	public void runsTestsFromFinderFieldsDeclaredAsDerivedTypes() {
		runner.run(SuiteWithClassFinderDeclaredAsSubtype.class);
		assertThat(executed.tests).contains("runnable1", "runnable2", "runnable3"); 
	}

	@Test
	public void appliesAllClassFilterFieldsAnnotatedWithFilter() {
		runner.run(SuiteThatFiltersOutAllClassesButOne.class);
		assertThat(executed.tests).containsOnly("runnable2");
	}

	@Test
	public void appliesOnlyCorrectlyDeclaredClassFilters() {
		runner.run(SuiteWithGoodAndBadClassFilters.class);
		assertThat(executed.tests).containsOnly("runnable1", "runnable2");
	}

	@Test
	public void appliesClassFilterFieldsDeclaredAsDerivedTypes() {
		runner.run(SuiteWithClassFilterDeclaredAsSubtype.class);
		assertThat(executed.tests).excludes("runnable2");
	}

	@Test
	public void runsAllMethodsIfNoFilters() {
		runner.run(SuiteWithNoMethodFilters.class);
		assertThat(executed.tests).contains("a_test1", "a_test2", "b_test1", "b_test2");
	}

	@Test
	public void runsOnlyTestMethodsThatSurviveMethodFilters() {
		runner.run(SuiteWithMethodFilters.class);
		assertThat(executed.tests).containsOnly("a_test1");
	}

	@Test
	public void ignoresNonTestMethods() {
		runner.run(SuiteWithAllBadTests.class);
		assertThat(executed.tests).isEmpty();
	}
}
