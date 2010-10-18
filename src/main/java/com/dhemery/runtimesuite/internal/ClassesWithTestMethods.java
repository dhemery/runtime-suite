package com.dhemery.runtimesuite.internal;

import java.lang.reflect.Method;

import org.junit.Test;

import com.dhemery.runtimesuite.ClassFilter;

public class ClassesWithTestMethods implements ClassFilter {

	@Override
	public boolean passes(Class<?> candidateClass) {
		return hasTestMethods(candidateClass);
	}

	private boolean hasTestMethods(Class<?> c) {
		for(Method method : c.getMethods()) {
			if(isTestMethod(method)) return true;
		}
		return false;
	}

	private boolean isTestMethod(Method method) {
		return hasTestAnnotation(method)
			&& takesNoParameters(method) 
			&& returnsVoid(method);
	}

	private boolean returnsVoid(Method method) {
		return method.getReturnType().equals(Void.TYPE);
	}

	private boolean takesNoParameters(Method method) {
		return method.getParameterTypes().length == 0;
	}

	private boolean hasTestAnnotation(Method method) {
		return method.isAnnotationPresent(Test.class);
	}
}
