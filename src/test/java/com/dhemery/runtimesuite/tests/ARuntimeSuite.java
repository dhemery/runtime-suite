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
import com.dhemery.runtimesuite.RuntimeSuite.ClassFilter;
import com.dhemery.runtimesuite.RuntimeSuite.Filter;
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
		public static boolean classFinder1WasRun = false;
		public static boolean classFinder2WasRun = false;
		@Finder public ClassFinder classFinder1 = new ClassFinder() {
			public List<Class<?>> find() {
				classFinder1WasRun = true;
				return Arrays.asList(new Class<?>[] { MyTestClass.class });
			}
		};
		@Finder public ClassFinder classFinder2 = new ClassFinder() {
			public List<Class<?>> find() {
				classFinder2WasRun = true;
				return Arrays.asList(new Class<?>[] { MyTestClass.class });
			}
		};
	}

	@Test public void runsTheSuiteClassFinders() throws InitializationError {
		new RuntimeSuite(MyClassFinderSuite.class, builder);
		assertThat(MyClassFinderSuite.classFinder1WasRun).isTrue();
		assertThat(MyClassFinderSuite.classFinder2WasRun).isTrue();
	}

	@RunWith(RuntimeSuite.class)
	public static class MyClassFilterSuite {
		public static boolean classFilter1WasRun = false;
		public static boolean classFilter2WasRun = false;
		
		@Finder public ClassFinder classFinder1 = new ClassFinder() {
			public List<Class<?>> find() {
				return Arrays.asList(new Class<?>[] { MyTestClass.class });
			}
		};

		@Filter public ClassFilter classiFilter1 = new ClassFilter() {
			public List<Class<?>> filter(List<Class<?>> candidateClasses) {
				classFilter1WasRun = true;
				return Arrays.asList(new Class<?>[] { MyTestClass.class });
			}
		};
		@Filter public ClassFilter classFilter2 = new ClassFilter() {
			public List<Class<?>> filter(List<Class<?>> candidateClasses) {
				classFilter2WasRun = true;
				return Arrays.asList(new Class<?>[] { MyTestClass.class });
			}
		};
	}
 
	@Test public void runsTheSuiteClassFilters() throws InitializationError, InstantiationException, IllegalAccessException {
		new RuntimeSuite(MyClassFilterSuite.class, builder);
		assertThat(MyClassFilterSuite.classFilter1WasRun).isTrue();
		assertThat(MyClassFilterSuite.classFilter2WasRun).isTrue();
	}
}

