package examples;

import com.dhemery.runtimesuite.ClassFilter;

public class ListedClassRejecterFilter extends ListedClassRejecterNonFilter implements ClassFilter {
	public ListedClassRejecterFilter(Class<?>...classesToRemove) {
		super(classesToRemove);
	}
}