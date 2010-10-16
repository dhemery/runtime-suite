package examples;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotAClassFilter /* does not implement ClassFilter */ {
	private List<Class<?>> classesToRemove;

	public NotAClassFilter(Class<?>...classesToRemove) {
		this.classesToRemove = Arrays.asList(classesToRemove);
	}

	public List<Class<?>> filter(List<Class<?>> candidateClasses) {
		List<Class<?>> filteredClasses = new ArrayList<Class<?>>();
		filteredClasses.addAll(candidateClasses);
		filteredClasses.removeAll(classesToRemove);
		return filteredClasses;
	}
}