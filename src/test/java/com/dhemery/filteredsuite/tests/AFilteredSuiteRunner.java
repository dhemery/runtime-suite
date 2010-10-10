package com.dhemery.filteredsuite.tests;

import java.util.Arrays;
import static org.fest.assertions.Assertions.*;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import com.dhemery.filteredsuite.ClassFinder;
import com.dhemery.filteredsuite.FilteredSuiteRunner;
import com.dhemery.filteredsuite.Finder;

import examples.MyTestClass;

public class AFilteredSuiteRunner {
	private RunnerBuilder fakeBuilder;

	@Before
	public void setUp() {
		fakeBuilder = new RunnerBuilder() {
			public Runner runnerForClass(Class<?> testClass) throws Throwable {
				return new Runner() {
					public Description getDescription() {
						return Description.EMPTY;
					}

					public void run(RunNotifier notifier) {
					}
					
				};
			}};

	}
	@RunWith(FilteredSuiteRunner.class)
	public static class MyClassFinderSuite {
		public static boolean myClassFinderWasRun = false;
		@Finder public ClassFinder myClassFinder = new ClassFinder() {
			public List<Class<?>> find(Class<?> suiteClass) {
				myClassFinderWasRun = true;
				return Arrays.asList(new Class<?>[] { MyTestClass.class });
			}
		};
	}

	@Test public void runsTheSuiteClassFinders() throws InitializationError, InstantiationException, IllegalAccessException {
		new FilteredSuiteRunner(MyClassFinderSuite.class, fakeBuilder);
		assertThat(MyClassFinderSuite.myClassFinderWasRun).isTrue();
	}
}
