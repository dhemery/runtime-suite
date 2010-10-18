package com.dhemery.runtimesuite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

import com.dhemery.runtimesuite.internal.ClassInspector;
import com.dhemery.runtimesuite.internal.ClassesWithTestMethods;

public class RuntimeSuite extends ParentRunner<Runner> {
	private List<Runner> runners;
	private Collection<ClassFinder> finders;
	private Collection<ClassFilter> filters;

	public RuntimeSuite(Class<?> suiteClass) throws InitializationError {
		super(suiteClass);
		ClassInspector inspector = new ClassInspector(suiteClass);
		finders = inspector.membersWith(Finder.class, ClassFinder.class);
		filters = inspector.membersWith(Filter.class, ClassFilter.class);
		filters.add(new ClassesWithTestMethods());
		runners = computeRunners(suiteClass);
	}

	protected Description describeChild(Runner child) {
		return child.getDescription();
	}

	protected List<Runner> getChildren() {	
		return getRunners();
	}

	protected void runChild(Runner child, RunNotifier notifier) {
		child.run(notifier);
	}

	public List<Runner> getRunners() {
		return runners;
	}

	private List<Runner> computeRunners(Class<?> suiteClass) throws InitializationError {
		Collection<Class<?>> candidateClasses = findTestClasses();
		Class<?>[] filteredClasses = filter(candidateClasses).toArray(new Class<?>[0]);
		return new AllDefaultPossibilitiesBuilder(false).runners(suiteClass, filteredClasses);
	}

	private Collection<Class<?>> findTestClasses() {
		Set<Class<?>> result = new HashSet<Class<?>>();
		for(ClassFinder finder : finders) {
			result.addAll(finder.find());
		}
		return result;
	}

	private Collection<Class<?>> filter(Collection<Class<?>> candidateClasses) {
		Collection<Class<?>> result = new ArrayList<Class<?>>();
		for(Class<?> candidateClass : candidateClasses) {
			if(passesAllFilters(candidateClass)) {
				result.add(candidateClass);
			}
		}
		return result;
	}

	private boolean passesAllFilters(Class<?> candidateClass) {
		for(ClassFilter filter : filters) {
			if(!filter.passes(candidateClass)) return false;
		}
		return true;
	}
}
