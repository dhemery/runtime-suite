package com.dhemery.runtimesuite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import examples.MyTestClass;

public class RuntimeSuite extends ParentRunner<Runner> {
	private List<Runner> runners;

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Finder {}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Filter {}

	public interface ClassFinder {
		List<Class<?>> find(Class<?> suiteClass);
	}

	public RuntimeSuite(Class<?> suiteClass, RunnerBuilder builder) throws InitializationError, InstantiationException, IllegalAccessException {
		super(suiteClass);
		List<Class<?>> candidateTestClasses = findTestClasses(suiteClass);
		List<Class<?>> testClasses = filterTestClasses(suiteClass, candidateTestClasses);
		runners = makeRunners(builder, testClasses);
		addFilter(suiteClass);
	}

	private List<Runner> makeRunners(RunnerBuilder builder, List<Class<?>> testClasses) throws InitializationError {
		List<Runner> runners = new ArrayList<Runner>();
		for(Class<?> testClass : testClasses) {
			runners.add(makeRunner(builder, testClass));
		}
		return runners;
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

	private List<Class<?>> filterTestClasses(Class<?> suiteClass, List<Class<?>> unfiltered) {
		return unfiltered;
	}

	private void addFilter(Class<?> suiteClass) {
		try {
			filter(new CategoryClassFilter(suiteClass));
		} catch (NoTestsRemainException e) {
		}
	}

	protected List<Runner> getChildren() {	
		return runners;
	}

	private List<Class<?>> findTestClasses(Class<?> suiteClass) throws InstantiationException, IllegalAccessException {
		List<Class<?>> result = new ArrayList<Class<?>>();
		result.add(MyTestClass.class);
		List<Field> classFinderFields = getClassFinderFields(suiteClass);
		System.out.println("found " + classFinderFields.size() + " class finder fields");
		for(Field field : classFinderFields) {
			result.addAll(getClasses(suiteClass, field));
		}
		return result;
	}

	private Collection<Class<?>> getClasses(Class<?> suiteClass, Field field) throws InstantiationException, IllegalAccessException {
		Object suite = field.getDeclaringClass().newInstance();
		ClassFinder finder = (ClassFinder) field.get(suite);
		return finder.find(suiteClass);
	}

	private List<Field> getClassFinderFields(Class<?> suiteClass) {
		List<Field> result = new ArrayList<Field>();
		Field[] fields = suiteClass.getFields();
		for(Field field : fields) {
			if(isClassFinderField(field)) {
				result.add(field);
			}
		}
		return result;
	}

	private boolean isClassFinderField(Field field) {
		boolean hasFinderAnnotation = field.isAnnotationPresent(Finder.class);
		boolean isClassFinder = field.getType().isAssignableFrom(ClassFinder.class);
		return hasFinderAnnotation && isClassFinder;
	}

	protected Description describeChild(Runner child) {
		return child.getDescription();
	}

	protected void runChild(Runner child, RunNotifier notifier) {
		child.run(notifier);
	}
}
