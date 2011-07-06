package example.filters;

import java.lang.reflect.Method;

import com.dhemery.runtimesuite.MethodFilter;

public class MethodsThatEndWith implements MethodFilter {
	private final String suffix;
	public MethodsThatEndWith(String suffix) {
		this.suffix = suffix;
	}
	public boolean passes(Method method) {
		return method.getName().endsWith(suffix);
	}
}