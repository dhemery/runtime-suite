package com.dhemery.runtimesuite.internal;

import java.util.List;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;


public class RunnableClassRunner extends BlockJUnit4ClassRunner {
	private static List<FrameworkMethod> runnableMethods;

	public RunnableClassRunner(RunnableClass runnable) throws InitializationError {
		// This calls the static method testClass() to initialize runnableMethods.
		// This is a horrible idea. But computeTestMethods() is called during
		// super(), and it needs runnableMethods. So I have to initialize
		// runnableMethods before calling super(). Gack.
		// TODO: Find a better way.
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
