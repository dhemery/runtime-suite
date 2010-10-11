package com.dhemery.runtimesuite.tests;

import java.util.Arrays;
import static org.fest.assertions.Assertions.*;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import com.dhemery.runtimesuite.RuntimeSuite;
import com.dhemery.runtimesuite.RuntimeSuite.ClassFinder;
import com.dhemery.runtimesuite.RuntimeSuite.Finder;

import examples.MyTestClass;

public class ARuntimeSuite {
	@Mock private RunnerBuilder builder;
	@Mock private Runner runner;

	@Before public void setUp() throws Throwable {
		MockitoAnnotations.initMocks(this);
		when(builder.runnerForClass(any(Class.class))).thenReturn(runner);
		when(runner.testCount()).thenReturn(1);
		when(runner.getDescription()).thenReturn(Description.TEST_MECHANISM);
	}

	@RunWith(RuntimeSuite.class)
	public static class MyClassFinderSuite {
		public static boolean myClassFinderWasRun = false;
		private List<Class<?>> testClasses = Arrays.asList(new Class<?>[] { MyTestClass.class });
		@Finder public ClassFinder myClassFinder = new ClassFinder() {
			public List<Class<?>> find(Class<?> suiteClass) {
				myClassFinderWasRun = true;
				return testClasses;
			}
		};
	}

	@Test public void runsTheSuiteClassFinders() throws InitializationError, InstantiationException, IllegalAccessException {
		new RuntimeSuite(MyClassFinderSuite.class, builder);
		assertThat(MyClassFinderSuite.myClassFinderWasRun).isTrue();
	}
}
