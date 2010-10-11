package com.dhemery.runtimesuite;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

public class RuntimeSuite extends ParentRunner<Runner> {
	public interface ClassFilter {
		List<Class<?>> filter(List<Class<?>> candidateClasses);
	}

	public interface ClassFinder {
		List<Class<?>> find();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Filter {}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Finder {}

	private List<Runner> runners;

	public RuntimeSuite(Class<?> suiteClass, RunnerBuilder builder) throws InitializationError {
		super(suiteClass);
		List<Field> classFinderFields = getClassFinderFields(suiteClass);
		List<Field> classFilterFields = getClassFilterFields(suiteClass);

		Object suite = makeSuite(suiteClass);

		List<Class<?>> candidateClasses = findTestClasses(suite, classFinderFields);
		List<Class<?>> testClasses = filterTestClasses(suite, classFilterFields, candidateClasses);

		runners = makeRunners(builder, testClasses);
	}

	private Object makeSuite(Class<?> suiteClass) throws InitializationError {
		try {
			return suiteClass.newInstance();
		} catch (Throwable cause) {
			throw new InitializationError(cause);
		}
	}

	private List<Class<?>> filterTestClasses(Object suite, List<Field> filterFields, List<Class<?>> candidateClasses) throws InitializationError {
		List<Class<?>> result = new ArrayList<Class<?>>();
		for(Field filterField : filterFields) {
			ClassFilter filter = (ClassFilter) getMember(suite, filterField);
			result = filter.filter(result);
		}
		return result;
	}

	private Object getMember(Object suite, Field memberField) throws InitializationError {
		try {
			return memberField.get(suite);
		} catch (Throwable cause) {
			throw new InitializationError(cause);
		}
	}

	protected Description describeChild(Runner child) {
		return child.getDescription();
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
		List<Class<?>> result = new ArrayList<Class<?>>();
		for(Field finderField : finderFields) {
			ClassFinder finder = (ClassFinder) getMember(suite, finderField);
			result.addAll(finder.find());
		}
		return result;
	}

	protected List<Runner> getChildren() {	
		return runners;
	}

	private List<Field> getClassFilterFields(Class<?> suiteClass) {
		return findMatchingFields(suiteClass, Filter.class, ClassFilter.class);
	}

	private List<Field> getClassFinderFields(Class<?> suiteClass) {
		return findMatchingFields(suiteClass, Finder.class, ClassFinder.class);
	}

	private boolean hasAnnotation(Field field,
			Class<? extends Annotation> requiredAnnotation) {
		return field.isAnnotationPresent(requiredAnnotation);
	}

	private boolean hasType(Field field, Class<?> requiredType) {
		return field.getType().isAssignableFrom(requiredType);
	}

	private Runner makeRunner(RunnerBuilder builder, Class<?> testClass) throws InitializationError {
		try {
			System.out.println("Trying to build a runner for " + testClass.getSimpleName());
			Runner runner = builder.runnerForClass(testClass);
			if(runner.testCount() < 1) {
				System.out.println("Found no test methods in " + testClass.getSimpleName());
				throw new NoTestsRemainException();
			}
			return runner;
		} catch (Throwable e) {
			System.out.println("runnerForClass() threw " + e.getClass().getSimpleName());
			throw new InitializationError(e);
		}
	}

	private List<Runner> makeRunners(RunnerBuilder builder, List<Class<?>> testClasses) throws InitializationError {
		List<Runner> runners = new ArrayList<Runner>();
		for(Class<?> testClass : testClasses) {
			runners.add(makeRunner(builder, testClass));
		}
		return runners;
	}

	protected void runChild(Runner child, RunNotifier notifier) {
		child.run(notifier);
	}
}
