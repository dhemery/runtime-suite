package com.dhemery.runtimesuite.internal;

import java.util.List;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;


public class RunnableClassRunner extends BlockJUnit4ClassRunner {
	private static List<FrameworkMethod> runnableMethods;

	public RunnableClassRunner(RunnableClass runnable) throws InitializationError {
		super(testClass(runnable));
	}

	private static Class<?> testClass(RunnableClass runnable) {
		runnableMethods = runnable.runnableMethods();
		return runnable.testClass();
	}

	@Override
	public List<FrameworkMethod> computeTestMethods() {
		return runnableMethods;	
	}
}
