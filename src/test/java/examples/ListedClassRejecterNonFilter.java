package examples;

import java.util.Arrays;
import java.util.List;

public class ListedClassRejecterNonFilter /* Does not implement ClassFilter */ {
	private List<Class<?>> classesToRemove;

	public ListedClassRejecterNonFilter(Class<?>...classesToRemove) {
		this.classesToRemove = Arrays.asList(classesToRemove);
	}

	public boolean passes(Class<?> candidateClass) {
		return !classesToRemove.contains(candidateClass);
	}
}