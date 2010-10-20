package com.dhemery.runtimesuite;

public interface ClassFilter {
	boolean passes(Class<?> candidateClass);
}