package com.dhemery.runtimesuite.tests;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import com.dhemery.runtimesuite.helpers.IsCollectionThat;
import com.dhemery.runtimesuite.helpers.SuiteRunListener;

import example.suites.SuiteThatFiltersOutAllClassesButOne;
import example.suites.SuiteThatFindsATestClassSeveralTimes;
import example.suites.SuiteWithAllBadTests;
import example.suites.SuiteWithClassFilterDeclaredAsSubtype;
import example.suites.SuiteWithClassFinderDeclaredAsSubtype;
import example.suites.SuiteWithGoodAndBadClassFilters;
import example.suites.SuiteWithGoodAndBadClassFinders;
import example.suites.SuiteWithMethodFilters;
import example.suites.SuiteWithNoMethodFilters;
import example.suites.SuiteWithTwoFinders;

public class ARuntimeSuite {
	private JUnitCore runner;
	private SuiteRunListener executed;

	@Before
	public void setUp() {
		executed = new SuiteRunListener();
		runner = new JUnitCore();
		runner.addListener(executed);
	}
	
	@Test
	public void runsTestsFromAllFinders() {
		runner.run(SuiteWithTwoFinders.class);
		assertThat(executed.tests, hasItems("runnable2", "runnable2")); 
	}

	@Test
	public void runsEachTestOnceEvenIfFoundMultipleTimes() {
		runner.run(SuiteThatFindsATestClassSeveralTimes.class);
		assertThat(executed.tests, not(hasDuplicates()));
	}

	private Matcher<List<String>> hasDuplicates() {
		return IsCollectionThat.<String>hasDuplicates();
	}
	
	@Test
	public void runsTestsOnlyFromCorrectlyDeclaredFinders() {
		runner.run(SuiteWithGoodAndBadClassFinders.class);
		assertThat(executed.tests, hasItems("runnable3")); 
	}

	@Test
	public void runsTestsFromFinderFieldsDeclaredAsDerivedTypes() {
		runner.run(SuiteWithClassFinderDeclaredAsSubtype.class);
		assertThat(executed.tests, hasItems("runnable1", "runnable2", "runnable3")); 
	}
 
	@Test
	public void appliesAllClassFilterFieldsAnnotatedWithFilter() {
		runner.run(SuiteThatFiltersOutAllClassesButOne.class);
		assertThat(executed.tests, hasItems("runnable2"));
	}

	@Test
	public void appliesOnlyCorrectlyDeclaredClassFilters() {
		runner.run(SuiteWithGoodAndBadClassFilters.class);
		assertThat(executed.tests, containsInAnyOrder("runnable1", "runnable2"));
	}

	@Test
	public void appliesClassFilterFieldsDeclaredAsDerivedTypes() {
		runner.run(SuiteWithClassFilterDeclaredAsSubtype.class);
		assertThat(executed.tests, not(contains("runnable2")));
	}

	@Test
	public void runsAllMethodsIfNoFilters() {
		runner.run(SuiteWithNoMethodFilters.class);
		assertThat(executed.tests, contains("a_test1", "a_test2", "b_test1", "b_test2"));
	}

	@Test
	public void runsOnlyTestMethodsThatSurviveMethodFilters() {
		runner.run(SuiteWithMethodFilters.class);
		assertThat(executed.tests, contains("a_test1"));
	}

	@Test
	public void ignoresNonTestMethods() {
		runner.run(SuiteWithAllBadTests.class);
		assertThat(executed.tests, hasSize(0));
	}
}
