package com.dhemery.runtimesuite;

import java.lang.reflect.Method;

public interface MethodFilter {
	boolean passes(Method method);
}
