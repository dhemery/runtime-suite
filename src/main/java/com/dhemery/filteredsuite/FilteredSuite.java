package com.dhemery.filteredsuite;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import com.dhemery.filteredsuite.examples.MyTestClass;

public class FilteredSuite extends ParentRunner<Runner> {
	private final Class<?> suiteClass;
	private List<Runner> runners;

	public FilteredSuite(Class<?> suiteClass, RunnerBuilder builder) throws InitializationError {
		super(suiteClass);
		System.out.format("FilteredSuite(%s)%n", suiteClass.getSimpleName());
		this.suiteClass = suiteClass;
		runners = makeRunners(builder);
		addFilter();
	}

	private List<Runner> makeRunners(RunnerBuilder builder) throws InitializationError {
		List<Runner> runners = new ArrayList<Runner>();
		for(Class<?> each : getFilteredTestClasses()) {
			try {
				Runner runner = builder.runnerForClass(each);
				if(runner.testCount() > 0) {
					runners.add(runner);					
				}
			} catch (Throwable e) {
				throw new InitializationError(e);
			}
		}
		return runners;
	}

	private List<Class<?>> getFilteredTestClasses() {
		return filterTestClasses(findTestClasses());
	}

	private List<Class<?>> filterTestClasses(List<Class<?>> unfiltered) {
		return unfiltered;
	}

	private void addFilter() {
		try {
			filter(new CategoryClassFilter(suiteClass));
		} catch (NoTestsRemainException e) {
		}
	}

	protected List<Runner> getChildren() {	
		return runners;
	}

	private List<Class<?>> findTestClasses() {
		List<Class<?>> result = new ArrayList<Class<?>>();
		result.add(MyTestClass.class);
		return result;
	}

	protected Description describeChild(Runner child) {
		return child.getDescription();
	}

	protected void runChild(Runner child, RunNotifier notifier) {
		child.run(notifier);
	}
}
