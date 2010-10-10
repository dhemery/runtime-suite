package com.dhemery.filteredsuite;

import java.util.List;

public interface ClassFinder {
	List<Class<?>> find(Class<?> suiteClass);
}
