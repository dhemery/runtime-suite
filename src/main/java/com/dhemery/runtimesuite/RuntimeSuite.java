package com.dhemery.runtimesuite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

public class RuntimeSuite extends ParentRunner<Runner> {
	private List<Runner> runners;

	public RuntimeSuite(Class<?> suiteClass) throws InitializationError {
		super(suiteClass);
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
		ClassInspector inspector = new ClassInspector(suiteClass);
		List<ClassFinder> finders = inspector.matchingMembers(Finder.class, ClassFinder.class);
		List<ClassFilter> filters = inspector.matchingMembers(Filter.class, ClassFilter.class);
		List<Class<?>> candidateClasses = findTestClasses(finders);
		Class<?>[] filteredClasses = filterTestClasses(filters, candidateClasses).toArray(new Class<?>[0]);
		return new AllDefaultPossibilitiesBuilder(false).runners(suiteClass, filteredClasses);
	}

	private List<Class<?>> findTestClasses(List<ClassFinder> finders) {
		List<Class<?>> result = new ArrayList<Class<?>>();
		for(ClassFinder finder : finders) {
			addNewClasses(result, finder.find());
		}
		return result;
	}

	private List<Class<?>> filterTestClasses(List<ClassFilter> filters, List<Class<?>> candidateClasses) {
		List<Class<?>> result = new ArrayList<Class<?>>();
		for(Class<?> candidateClass : candidateClasses) {
			if(passesAllFilters(filters, candidateClass)) {
				result.add(candidateClass);
			}
		}
		return result;
	}

	private boolean passesAllFilters(List<ClassFilter> filters, Class<?> candidateClass) {
		for(ClassFilter filter : filters) {
			if(!filter.passes(candidateClass)) return false;
		}
		return true;
	}


	private void addNewClasses(Collection<Class<?>> knownClasses, Collection<Class<?>> classesToMakeKnown) {
		for(Class<?> classToMakeKnown : classesToMakeKnown) {
			if(!knownClasses.contains(classToMakeKnown)) knownClasses.add(classToMakeKnown);
		}
	}

}
