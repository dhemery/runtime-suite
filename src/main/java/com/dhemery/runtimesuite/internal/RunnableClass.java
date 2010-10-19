package com.dhemery.runtimesuite.internal;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;

import com.dhemery.runtimesuite.ClassFilter;
import com.dhemery.runtimesuite.MethodFilter;

public class RunnableClass {
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

	public List<FrameworkMethod> runnableMethods() {
		return runnableMethods;
	}

	private boolean passesClassFilters() {
		for(ClassFilter filter : classFilters) {
			if(!filter.passes(testClass)) return false;
		}
		return true;
	}

	private boolean hasTestAnnotation(Method method) {
		return method.isAnnotationPresent(Test.class);
	}

	private boolean isRunnable(Method method) {
		return hasTestAnnotation(method)
			&& takesNoParameters(method) 
			&& returnsVoid(method)
			&& passesFilters(method);
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

	private boolean takesNoParameters(Method method) {
		return method.getParameterTypes().length == 0;
	}

	public Class<?> testClass() {
		return testClass;
	}
}
