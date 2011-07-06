package example.filters;

import java.lang.reflect.Method;

import com.dhemery.runtimesuite.MethodFilter;

public class MethodsThatStartWith implements MethodFilter {
	private final String prefix;
	public MethodsThatStartWith(String prefix) {
		this.prefix = prefix;
	}
	public boolean passes(Method method) {
		return method.getName().startsWith(prefix);
	}
}