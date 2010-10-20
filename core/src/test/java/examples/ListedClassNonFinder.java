package examples;

import java.util.Arrays;
import java.util.Collection;

public class ListedClassNonFinder /* Does not implement ClassFinder */ {
	private Collection<Class<?>> classes;

	public ListedClassNonFinder(Class<?>...classes) {
		this.classes = Arrays.asList(classes);
	}

	public Collection<Class<?>> find() {
		return classes;
	}
}