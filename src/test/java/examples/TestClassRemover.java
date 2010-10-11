package examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dhemery.runtimesuite.RuntimeSuite.ClassFilter;

public class TestClassRemover implements ClassFilter {
	private List<Class<?>> classesToRemove;

	public TestClassRemover(Class<?>...classesToRemove) {
		this.classesToRemove = Arrays.asList(classesToRemove);
	}

	public List<Class<?>> filter(List<Class<?>> candidateClasses) {
		List<Class<?>> filteredClasses = new ArrayList<Class<?>>();
		filteredClasses.addAll(candidateClasses);
		filteredClasses.removeAll(classesToRemove);
		return filteredClasses;
	}
}