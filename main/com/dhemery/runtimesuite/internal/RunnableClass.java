package com.dhemery.runtimesuite.internal;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.MethodFilter;

/**
 * Reports runnable test methods to its parent {@link BlockJUnit4ClassRunner}
 * for execution.
 * This class is not intended for public use.
 * @author Dale H. Emery
 */
public class RunnableClass {
	public class RunnableClassRunner extends BlockJUnit4ClassRunner {
		public RunnableClassRunner() throws InitializationError {
			super(testClass);
		}

		@Override
		public List<FrameworkMethod> computeTestMethods() {
			return runnableMethods;	
		}
	}
	private final Collection<ClassFilter> classFilters;
	private final Collection<MethodFilter> methodFilters;
	private final Class<?> testClass;

	private List<FrameworkMethod> runnableMethods = new ArrayList<FrameworkMethod>();

	public RunnableClass(Class<?> testClass, Collection<ClassFilter> classFilters, Collection<MethodFilter> methodFilters) {
		this.testClass = testClass;
		this.classFilters = classFilters;
		this.methodFilters = methodFilters;
		if(passesClassFilters()) {
			gatherRunnableMethods();
		}
	}

	private void gatherRunnableMethods() {
		for(Method method : testClass.getMethods()) {
			if(isRunnable(method)) {
				runnableMethods.add(new FrameworkMethod(method));
			}
		}
	}

	private boolean hasTestAnnotation(Method method) {
		return method.isAnnotationPresent(Test.class);
	}

	public boolean isRunnable() {
		return !runnableMethods.isEmpty();
	}

	private boolean isRunnable(Method method) {
		return hasTestAnnotation(method)
			&& takesNoParameters(method) 
			&& returnsVoid(method)
			&& passesFilters(method);
	}

	private boolean passesClassFilters() {
		for(ClassFilter filter : classFilters) {
			if(!filter.passes(testClass)) return false;
		}
		return true;
	}

	private boolean passesFilters(Method method) {
		for(MethodFilter filter : methodFilters) {
			if(!filter.passes(method)) {
				return false;
			}
		}
		return true;
	}

	private boolean returnsVoid(Method method) {
		return method.getReturnType().equals(Void.TYPE);
	}

	public RunnableClassRunner runner() throws InitializationError {
		return new RunnableClassRunner();
	}

	private boolean takesNoParameters(Method method) {
		return method.getParameterTypes().length == 0;
	}

	public Class<?> testClass() {
		return testClass;
	}
}
