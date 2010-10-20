package examples;

import java.lang.reflect.Method;

import com.dhemery.runtimesuite.MethodFilter;

public class MethodNamePrefixFilter implements MethodFilter {
	private final String prefix;

	public MethodNamePrefixFilter(String prefix) {
		this.prefix = prefix;
	}

	public boolean passes(Method method) {
		return method.getName().startsWith(prefix);
	}
}