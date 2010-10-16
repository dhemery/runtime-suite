package examples;


import java.util.Arrays;
import java.util.List;

import com.dhemery.runtimesuite.ClassFinder;

public class TestClassFinder implements ClassFinder {
	private List<Class<?>> classes;

	public TestClassFinder(Class<?>...classes) {
		this.classes = Arrays.asList(classes);
	}

	public Collection<Class<?>> find() {
		return classes;
	}

}
