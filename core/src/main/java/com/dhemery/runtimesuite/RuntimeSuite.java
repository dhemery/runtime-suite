package com.dhemery.runtimesuite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

import com.dhemery.runtimesuite.internal.SuiteInspector;
import com.dhemery.runtimesuite.internal.RunnableClass;

public class RuntimeSuite extends ParentRunner<Runner> {
	private final List<Runner> runners;
	private final SuiteInspector inspector;
	private final List<ClassFinder> classFinders;
	private final List<ClassFilter> classFilters;
	private final List<MethodFilter> methodFilters;

	public RuntimeSuite(Class<?> suiteClass) throws InitializationError {
		super(suiteClass);
		inspector = new SuiteInspector(suiteClass);
		classFinders = inspector.classFinders();
		classFilters = inspector.classFilters();
		methodFilters = inspector.methodFilters();
		runners = runnersFor(classesInSuite());
	}

	private Set<Class<?>> classesInSuite() throws InitializationError {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		for(ClassFinder finder : classFinders) {
			classes.addAll(finder.find());
		}
		return classes;
	}

	protected Description describeChild(Runner child) {
		return child.getDescription();
	}

	protected List<Runner> getChildren() {	
		return getRunners();
	}

	public List<Runner> getRunners() {
		return runners;
	}

	protected void runChild(Runner child, RunNotifier notifier) {
		child.run(notifier);
	}

	private List<Runner> runnersFor(Collection<Class<?>> classes) throws InitializationError {
		List<Runner> runners = new ArrayList<Runner>();
		for(Class<?> c : classes) {
			RunnableClass runnable = new RunnableClass(c, classFilters, methodFilters);
			if(runnable.isRunnable()) {
				runners.add(runnable.runner());
			}
		}
		return runners;
	}
}
