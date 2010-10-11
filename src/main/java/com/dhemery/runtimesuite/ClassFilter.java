package com.dhemery.runtimesuite;

import java.util.List;

public interface ClassFilter {
	List<Class<?>> filter(List<Class<?>> candidateClasses);
}