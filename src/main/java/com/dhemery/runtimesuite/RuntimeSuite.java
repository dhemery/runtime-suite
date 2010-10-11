package com.dhemery.runtimesuite;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

public class RuntimeSuite extends ParentRunner<Runner> {
	private final Class<?> suiteClass;
	private final RunnerBuilder builder;

	public RuntimeSuite(Class<?> suiteClass, RunnerBuilder builder) throws InitializationError {
		super(suiteClass);
		this.suiteClass = suiteClass;
		this.builder = builder;
	}

	private void addNewClasses(List<Class<?>> knownClasses, List<Class<?>> classesToMakeKnown) {
		for(Class<?> classToMakeKnown : classesToMakeKnown) {
			if(!knownClasses.contains(classToMakeKnown)) knownClasses.add(classToMakeKnown);
		}
	}

	private List<Class<?>> applyFilters(List<ClassFilter> filters, List<Class<?>> candidateClasses) {
		List<Class<?>> result = new ArrayList<Class<?>>();
		for(Class<?> candidateClass : candidateClasses) {
			if(passesAllFilters(filters, candidateClass)) result.add(candidateClass);
		}
		return result;
	}

	private List<Runner> computeRunners(Class<?> suiteClass, RunnerBuilder builder) throws InitializationError {
		List<Field> classFinderFields = getClassFinderFields(suiteClass);
		List<Field> classFilterFields = getClassFilterFields(suiteClass);

		Object suite = makeSuite(suiteClass);

		List<Class<?>> candidateClasses = findTestClasses(suite, classFinderFields);
		List<Class<?>> testClasses = filterTestClasses(suite, classFilterFields, candidateClasses);

		return makeRunners(builder, testClasses);
	}

	protected Description describeChild(Runner child) {
		return child.getDescription();
	}

	private List<Class<?>> filterTestClasses(Object suite, List<Field> filterFields, List<Class<?>> candidateClasses) throws InitializationError {
		if(filterFields.isEmpty()) return candidateClasses;
		List<ClassFilter> filters = makeFilters(suite, filterFields);
		return applyFilters(filters, candidateClasses);
	}

	private List<Field> findMatchingFields(Class<?> suiteClass, Class<? extends Annotation> requiredAnnotation, Class<?> requiredType) {
		Field[] fields = suiteClass.getFields();
		List<Field> result = new ArrayList<Field>();
		for(Field field : fields) {
			if(hasAnnotation(field, requiredAnnotation) && hasType(field, requiredType)) {
				result.add(field);
			}
		}
		return result;
	}

	private List<Class<?>> findTestClasses(Object suite, List<Field> finderFields) throws InitializationError {
		return runFinders(makeFinders(suite, finderFields));
	}

	protected List<Runner> getChildren() {	
		return getRunners();
	}

	private List<Field> getClassFilterFields(Class<?> suiteClass) {
		return findMatchingFields(suiteClass, Filter.class, ClassFilter.class);
	}

	private List<Field> getClassFinderFields(Class<?> suiteClass) {
		return findMatchingFields(suiteClass, Finder.class, ClassFinder.class);
	}

	private Object getMember(Object suite, Field memberField) throws InitializationError {
		try {
			return memberField.get(suite);
		} catch (Throwable cause) {
			throw new InitializationError(cause);
		}
	}

	public List<Runner> getRunners() {
		try {
			return computeRunners(suiteClass, builder);
		} catch (InitializationError cause) {
			return Collections.emptyList();
		}
	}

	private boolean hasAnnotation(Field field, Class<? extends Annotation> requiredAnnotation) {
		return field.isAnnotationPresent(requiredAnnotation);
	}

	private boolean hasType(Field field, Class<?> requiredType) {
		return requiredType.isAssignableFrom(field.getType());
	}

	private List<ClassFilter> makeFilters(Object suite, List<Field> filterFields)
			throws InitializationError {
		List<ClassFilter> filters = new ArrayList<ClassFilter>();
		for(Field filterField : filterFields) {
			ClassFilter filter = (ClassFilter) getMember(suite, filterField);
			filters.add(filter);
		}
		return filters;
	}

	private List<ClassFinder> makeFinders(Object suite, List<Field> finderFields)
			throws InitializationError {
		List<ClassFinder> finders = new ArrayList<ClassFinder>();
		for(Field finderField : finderFields) {
			finders.add((ClassFinder) getMember(suite, finderField));
		}
		return finders;
	}

	private Runner makeRunner(RunnerBuilder builder, Class<?> testClass) throws InitializationError {
		return new BlockJUnit4ClassRunner(testClass);
	}

	private List<Runner> makeRunners(RunnerBuilder builder, List<Class<?>> testClasses) {
		List<Runner> runners = new ArrayList<Runner>();
		for(Class<?> testClass : testClasses) {
			try {
				runners.add(makeRunner(builder, testClass));
			} catch (InitializationError e) {}
		}
		return runners;
	}

	private Object makeSuite(Class<?> suiteClass) throws InitializationError {
		try {
			return suiteClass.newInstance();
		} catch (Throwable cause) {
			throw new InitializationError(cause);
		}
	}

	private boolean passesAllFilters(List<ClassFilter> filters, Class<?> candidateClass) {
		for(ClassFilter filter : filters) {
			if(!filter.passes(candidateClass)) return false;
		}
		return true;
	}

	protected void runChild(Runner child, RunNotifier notifier) {
		child.run(notifier);
	}

	private List<Class<?>> runFinders(List<ClassFinder> finders) {
		List<Class<?>> result = new ArrayList<Class<?>>();
		for(ClassFinder finder : finders) {
			addNewClasses(result, finder.find());
		}
		return result;
	}
}
